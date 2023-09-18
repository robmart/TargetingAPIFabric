package robmart.mod.targetingapifabric.api.faction;

import com.google.common.collect.Sets;
import dev.onyxstudios.cca.api.v3.level.LevelComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import robmart.mod.targetingapifabric.api.TAPILevelComponents;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.reference.Reference;

import java.util.*;

/**
 * Created by Jacob on 4/19/2018.
 * Remade by Robmart on 1/30/2020
 */
public class Faction implements IFaction {
    private final boolean isPermanent;

    private final List<Class<? extends Entity>> memberClasses = new ArrayList<>();
    private final List<Class<? extends Entity>> friendClasses = new ArrayList<>();
    private final List<Class<? extends Entity>> enemyClasses = new ArrayList<>();
    private final Map<String, Boolean> memberEntities = new HashMap<>();
    private final Map<String, Boolean> friendEntities = new HashMap<>();
    private final Map<String, Boolean> enemyEntities = new HashMap<>();

    private final boolean isServerSide;

    private String name;

    public Faction(String name) {
        this(name, true);
    }

    public Faction(String name, boolean permanent) {
        this(name, permanent, true);
    }

    public Faction(String name, boolean permanent, boolean isServerSide) {
        super();
        this.name = name;
        this.isPermanent = permanent;
        this.isServerSide = isServerSide;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean setName(String name) {
        if (Targeting.getFaction(name) != null)
            return false;
        else {
            this.name = name;
            sync();
            return true;
        }
    }

    @Override
    public boolean getIsPermanent() {
        return this.isPermanent;
    }

    @Override
    public void addFriendClass(Class<? extends Entity> classToAdd) {
        if (!isFriend(classToAdd)) {
            this.friendClasses.add(classToAdd);
            sync();
        }
    }

    @Override
    public void addFriendEntity(Entity entityToAdd) {
        if (!isFriend(entityToAdd)) {
            if (entityToAdd instanceof PlayerEntity)
                this.friendEntities.put(entityToAdd.getEntityName(), true);
            else
                this.friendEntities.put(entityToAdd.getEntityName(), false);

            sync();
        }
    }

    @Override
    public void removeFriendClass(Class<? extends Entity> classToRemove) {
        if (isFriend(classToRemove)) {
            this.friendClasses.remove(classToRemove);
            sync();
        }
    }

    @Override
    public void removeFriendEntity(Entity entityToRemove) {
        if (isFriend(entityToRemove)) {
            this.friendEntities.remove(entityToRemove.getEntityName());
            sync();
        }
    }

    @Override
    public void addMemberClass(Class<? extends Entity> classToAdd){
        if (!isMember(classToAdd)) {
            this.memberClasses.add(classToAdd);
            sync();
        }
    }

    @Override
    public void addMemberEntity(Entity entityToAdd){
        if (!isMember(entityToAdd)) {
            if (entityToAdd instanceof PlayerEntity)
                this.memberEntities.put(entityToAdd.getEntityName(), true);
            else
                this.memberEntities.put(entityToAdd.getEntityName(), false);

            sync();
        }
    }

    @Override
    public void removeMemberClass(Class<? extends Entity> classToRemove) {
        if (isMember(classToRemove)) {
            this.memberClasses.remove(classToRemove);
            sync();
        }
    }

    @Override
    public void removeMemberEntity(Entity entityToRemove) {
        if (isMember(entityToRemove)) {
            this.memberEntities.remove(entityToRemove.getEntityName());
            sync();
        }
    }

    @Override
    public void addEnemyClass(Class<? extends Entity> classToAdd) {
        if (!isEnemy(classToAdd)) {
            this.enemyClasses.add(classToAdd);
            sync();
        }
    }

    @Override
    public void addEnemyEntity(Entity entityToAdd) {
        if (!isEnemy(entityToAdd)) {
            if (entityToAdd instanceof PlayerEntity)
                this.enemyEntities.put(entityToAdd.getEntityName(), true);
            else
                this.enemyEntities.put(entityToAdd.getEntityName(), false);

            sync();
        }
    }

    @Override
    public void removeEnemyClass(Class<? extends Entity> classToRemove) {
        if (isEnemy(classToRemove)) {
            this.enemyClasses.remove(classToRemove);
            sync();
        }
    }

    @Override
    public void removeEnemyEntity(Entity entityToRemove) {
        if (isEnemy(entityToRemove)) {
            this.enemyEntities.remove(entityToRemove.getEntityName());
            sync();
        }
    }

    @Override
    public Map<Object, Boolean> getAllMembers() {
        Map<Object, Boolean> memberSet = new HashMap<>();
        this.memberClasses.forEach(mclass -> memberSet.put(mclass, false));
        memberSet.putAll(this.memberEntities);
        return memberSet;
    }

    @Override
    public Map<Object, Boolean> getAllFriends() {
        Map<Object, Boolean> friendSet = new HashMap<>();
        this.friendClasses.forEach(fclass -> friendSet.put(fclass, false));
        friendSet.putAll(this.friendEntities);
        return friendSet;
    }

    @Override
    public Map<Object, Boolean> getAllEnemies() {
        Map<Object, Boolean> enemySet = new HashMap<>();
        this.enemyClasses.forEach(eclass -> enemySet.put(eclass, false));
        enemySet.putAll(this.enemyEntities);
        return enemySet;
    }

    @Override
    public void clearMembers(){
        this.memberClasses.clear();
        this.memberEntities.clear();
        sync();
    }

    @Override
    public void clearFriends() {
        this.friendClasses.clear();
        this.friendEntities.clear();
        sync();
    }

    @Override
    public void clearEnemies() {
        this.enemyClasses.clear();
        this.enemyEntities.clear();
        sync();
    }

    @Override
    public boolean isMember(Class<? extends Entity> potentialMember){
        if (potentialMember == null) return false;
        for (Class<? extends Entity> member : this.memberClasses){
            if (member.isAssignableFrom(potentialMember) || potentialMember.isAssignableFrom(member))
                return true;
        }

        return false;
    }

    @Override
    public boolean isMember(Entity potentialMember){
        if (potentialMember == null) return false;
        if (isMember(potentialMember.getClass())) return true;
        for (String entity : this.memberEntities.keySet()) {
            if (entity.equals(potentialMember.getEntityName())) return true;
        }

        return false;
    }

    @Override
    public boolean isFriend(Class<? extends Entity> potentialFriend){
        if (potentialFriend == null) return false;
        for (Class<? extends Entity> member : this.friendClasses){
            if (member.isAssignableFrom(potentialFriend) || potentialFriend.isAssignableFrom(member))
                return true;
        }

        return false;
    }

    @Override
    public boolean isFriend(Entity potentialFriend){
        if (potentialFriend == null) return false;
        if (isFriend(potentialFriend.getClass())) return true;
        for (String entity : this.friendEntities.keySet()) {
            if (entity.equals(potentialFriend.getEntityName())) return true;
        }

        return false;
    }

    @Override
    public boolean isEnemy(Class<? extends Entity> potentialEnemy){
        if (potentialEnemy == null) return false;
        for (Class<? extends Entity> member : this.enemyClasses){
            if (member.isAssignableFrom(potentialEnemy) || potentialEnemy.isAssignableFrom(member))
                return true;
        }

        return false;
    }

    @Override
    public boolean isEnemy(Entity potentialEnemy){
        if (potentialEnemy == null) return false;
        if (isFriend(potentialEnemy.getClass())) return true;
        for (String entity : this.enemyEntities.keySet()) {
            if (entity.equals(potentialEnemy.getEntityName())) return true;
        }

        return false;
    }

    @Override
    public void onDisband() {

    }

    @Override
    public void sync() {
        System.out.println("SYNC " + (this.isServerSide) + " " + (Reference.MINECRAFT_SERVER != null));
        if (this.isServerSide && Reference.MINECRAFT_SERVER != null)
            LevelComponents.sync(TAPILevelComponents.FACTION_MANAGER, Reference.MINECRAFT_SERVER);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        //Classes
        int i = 0;
        while (tag.contains("MemberClass" + i)) {
            try {
                //Gets the class from string and adds it to the faction
                addMemberClass((Class<? extends Entity>) Class.forName(tag.getString("MemberClass" + i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }

        i = 0;
        while (tag.contains("FriendClass" + i)) {
            try {
                //Gets the class from string and adds it to the faction
                addFriendClass((Class<? extends Entity>) Class.forName(tag.getString("FriendClass" + i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }

        i = 0;
        while (tag.contains("EnemyClass" + i)) {
            try {
                //Gets the class from string and adds it to the faction
                addEnemyClass((Class<? extends Entity>) Class.forName(tag.getString("EnemyClass" + i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            i++;
        }

        //Entities
        i = 0;
        while (tag.contains("MemberEntity" + i + "Key")) {
            memberEntities.put(tag.getString("MemberEntity" + i + "Key"), tag.getBoolean("MemberEntity" + i + "Value"));
            i++;
        }

        i = 0;
        while (tag.contains("FriendEntity" + i + "Key")) {
            memberEntities.put(tag.getString("FriendEntity" + i + "Key"), tag.getBoolean("FriendEntity" + i + "Value"));
            i++;
        }

        i = 0;
        while (tag.contains("EnemyEntity" + i + "Key")) {
            memberEntities.put(tag.getString("EnemyEntity" + i + "Key"), tag.getBoolean("EnemyEntity" + i + "Value"));
            i++;
        }
    }

    @Override
    public NbtCompound writeToNbt(NbtCompound compound) {
        if (compound == null) return null;
        if (!getIsPermanent()) return compound;

        compound.putString("Class", this.getClass().getName());
        compound.putString("Name", getName());

        memberClasses.forEach(mclass -> compound.putString("MemberClass" + memberClasses.indexOf(mclass), mclass.getName()));
        friendClasses.forEach(fclass -> compound.putString("FriendClass" + friendClasses.indexOf(fclass), fclass.getName()));
        enemyClasses.forEach(eclass -> compound.putString("EnemyClass" + enemyClasses.indexOf(eclass), eclass.getName()));

        final int[] i = {0};
        memberEntities.forEach((mentity, isPlayer) -> {
            compound.putString("MemberEntity" + i[0] + "Key", mentity);
            compound.putBoolean("MemberEntity" + i[0]++ + "Value", isPlayer);
        });
        i[0] = 0;
        friendEntities.forEach((fentity, isPlayer) -> {
            compound.putString("FriendEntity" + i[0] + "Key", fentity);
            compound.putBoolean("FriendEntity" + i[0]++ + "Value", isPlayer);
        });
        i[0] = 0;
        enemyEntities.forEach((eentity, isPlayer) -> {
            compound.putString("EnemyEntity" + i[0] + "Key", eentity);
            compound.putBoolean("EnemyEntity" + i[0]++ + "Value", isPlayer);
        });

        return compound;
    }

    @Override
    public String toString() {
        return "Faction: { " + getName() + " }";
    }
}

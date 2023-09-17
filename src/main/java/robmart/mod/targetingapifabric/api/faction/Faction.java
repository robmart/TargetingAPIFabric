package robmart.mod.targetingapifabric.api.faction;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
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
    private final List<String> memberEntities = new ArrayList<>();
    private final List<String> friendEntities = new ArrayList<>();
    private final List<String> enemyEntities = new ArrayList<>();

    private String name;

    public Faction(String name) {
        this(name, true);
    }

    public Faction(String name, boolean permanent) {
        super();
        this.name = name;
        this.isPermanent = permanent;
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
            return true;
        }
    }

    @Override
    public boolean getIsPermanent() {
        return this.isPermanent;
    }

    @Override
    public void addFriendClass(Class<? extends Entity> classToAdd) {
        if (!isFriend(classToAdd))
            this.friendClasses.add(classToAdd);


    }

    @Override
    public void addFriendEntity(Entity entityToAdd) {
        if (!isFriend(entityToAdd))
            this.friendEntities.add(entityToAdd.getEntityName());


    }

    @Override
    public void removeFriendClass(Class<? extends Entity> classToRemove) {
        if (isFriend(classToRemove))
            this.friendClasses.remove(classToRemove);


    }

    @Override
    public void removeFriendEntity(Entity entityToRemove) {
        if (isFriend(entityToRemove))
            this.friendEntities.remove(entityToRemove.getEntityName());


    }

    @Override
    public void addMemberClass(Class<? extends Entity> classToAdd){
        if (!isMember(classToAdd))
            this.memberClasses.add(classToAdd);


    }

    @Override
    public void addMemberEntity(Entity entityToAdd){
        if (!isMember(entityToAdd))
            this.memberEntities.add(entityToAdd.getEntityName());


    }

    @Override
    public void removeMemberClass(Class<? extends Entity> classToRemove) {
        if (isMember(classToRemove))
            this.memberClasses.remove(classToRemove);


    }

    @Override
    public void removeMemberEntity(Entity entityToRemove) {
        if (isMember(entityToRemove))
            this.memberEntities.remove(entityToRemove.getEntityName());


    }

    @Override
    public void addEnemyClass(Class<? extends Entity> classToAdd) {
        if (!isEnemy(classToAdd))
            this.enemyClasses.add(classToAdd);


    }

    @Override
    public void addEnemyEntity(Entity entityToAdd) {
        if (!isEnemy(entityToAdd))
            this.enemyEntities.add(entityToAdd.getEntityName());


    }

    @Override
    public void removeEnemyClass(Class<? extends Entity> classToRemove) {
        if (isEnemy(classToRemove))
            this.enemyClasses.remove(classToRemove);


    }

    @Override
    public void removeEnemyEntity(Entity entityToRemove) {
        if (isEnemy(entityToRemove))
            this.enemyEntities.remove(entityToRemove.getEntityName());


    }

    @Override
    public Set<Object> getAllMembers() {
        Set<Object> memberSet = Sets.newHashSet();
        memberSet.addAll(this.memberClasses);
        memberSet.addAll(this.memberEntities);
        return memberSet;
    }

    @Override
    public Set<Object> getAllFriends() {
        Set<Object> friendSet = Sets.newHashSet();
        friendSet.addAll(this.friendClasses);
        friendSet.addAll(this.friendEntities);
        return friendSet;
    }

    @Override
    public Set<Object> getAllEnemies() {
        Set<Object> enemySet = Sets.newHashSet();
        enemySet.addAll(this.enemyClasses);
        enemySet.addAll(this.enemyEntities);
        return enemySet;
    }

    @Override
    public void clearMembers(){
        this.memberClasses.clear();
        this.memberEntities.clear();


    }

    @Override
    public void clearFriends() {
        this.friendClasses.clear();
        this.friendEntities.clear();


    }

    @Override
    public void clearEnemies() {
        this.enemyClasses.clear();
        this.enemyEntities.clear();


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
        for (String entity : this.memberEntities) {
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
        for (String entity : this.friendEntities) {
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
        for (String entity : this.enemyEntities) {
            if (entity.equals(potentialEnemy.getEntityName())) return true;
        }

        return false;
    }

    @Override
    public void onDisband() {

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
        while (tag.contains("MemberEntity" + i)) {
            memberEntities.add(tag.getString("MemberEntity" + i));
            i++;
        }

        i = 0;
        while (tag.contains("FriendEntity" + i)) {
            memberEntities.add(tag.getString("FriendEntity" + i));
            i++;
        }

        i = 0;
        while (tag.contains("EnemyEntity" + i)) {
            memberEntities.add(tag.getString("EnemyEntity" + i));
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

        memberEntities.forEach(mentity -> compound.putString("MemberEntity" + memberEntities.indexOf(mentity), mentity));
        friendEntities.forEach(fentity -> compound.putString("FriendEntity" + friendEntities.indexOf(fentity), fentity));
        enemyEntities.forEach(eentity -> compound.putString("EnemyEntity" + enemyEntities.indexOf(eentity), eentity));

        return compound;
    }

    @Override
    public String toString() {
        return "Faction: { " + getName() + " }";
    }
}

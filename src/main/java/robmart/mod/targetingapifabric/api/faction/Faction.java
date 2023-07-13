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
    private final List<Entity> memberEntities = new ArrayList<>();
    private final List<Entity> friendEntities = new ArrayList<>();
    private final List<Entity> enemyEntities = new ArrayList<>();

    private final Map<String, UUID> unprocessedData = new HashMap<>();

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
        if (Targeting.getFaction(name) == null) return false;

        this.name = name;
        return true;
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
            this.friendEntities.add(entityToAdd);


    }

    @Override
    public void removeFriendClass(Class<? extends Entity> classToRemove) {
        if (isFriend(classToRemove))
            this.friendClasses.remove(classToRemove);


    }

    @Override
    public void removeFriendEntity(Entity entityToRemove) {
        if (isFriend(entityToRemove))
            this.friendEntities.remove(entityToRemove);


    }

    @Override
    public void addMemberClass(Class<? extends Entity> classToAdd){
        if (!isMember(classToAdd))
            this.memberClasses.add(classToAdd);


    }

    @Override
    public void addMemberEntity(Entity entityToAdd){
        if (!isMember(entityToAdd))
            this.memberEntities.add(entityToAdd);


    }

    @Override
    public void removeMemberClass(Class<? extends Entity> classToRemove) {
        if (isMember(classToRemove))
            this.memberClasses.remove(classToRemove);


    }

    @Override
    public void removeMemberEntity(Entity entityToRemove) {
        if (isMember(entityToRemove))
            this.memberEntities.remove(entityToRemove);


    }

    @Override
    public void addEnemyClass(Class<? extends Entity> classToAdd) {
        if (!isEnemy(classToAdd))
            this.enemyClasses.add(classToAdd);


    }

    @Override
    public void addEnemyEntity(Entity entityToAdd) {
        if (!isEnemy(entityToAdd))
            this.enemyEntities.add(entityToAdd);


    }

    @Override
    public void removeEnemyClass(Class<? extends Entity> classToRemove) {
        if (isEnemy(classToRemove))
            this.enemyClasses.remove(classToRemove);


    }

    @Override
    public void removeEnemyEntity(Entity entityToRemove) {
        if (isEnemy(entityToRemove))
            this.enemyEntities.remove(entityToRemove);


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
        for (Entity entity : this.memberEntities) {
            if (entity.equals(potentialMember)) return true;
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
        for (Entity entity : this.friendEntities) {
            if (entity.equals(potentialFriend)) return true;
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
        for (Entity entity : this.enemyEntities) {
            if (entity.equals(potentialEnemy)) return true;
        }

        return false;
    }

    @Override
    public void refreshPlayers() {
        Iterator<Map.Entry<String, UUID>> iterator = unprocessedData.entrySet().iterator();
       while (iterator.hasNext()) {
           Map.Entry<String, UUID> entry = iterator.next();
           String key = entry.getKey();
           UUID value = entry.getValue();

            if (key.contains("Member")) {
                if (Reference.MINECRAFT_SERVER != null) {

                    Entity entity = Reference.MINECRAFT_SERVER.getOverworld().getEntity(value);
                    if (entity != null) {
                        addMemberEntity(entity);
                        iterator.remove();
                    }
                }
            }

            if (key.contains("Friend")) {
                if (Reference.MINECRAFT_SERVER != null) {

                    Entity entity = Reference.MINECRAFT_SERVER.getOverworld().getEntity(value);
                    if (entity != null) {
                        addFriendEntity(entity);
                        iterator.remove();
                    }
                }
            }

            if (key.contains("Enemy")) {
                if (Reference.MINECRAFT_SERVER != null) {

                    Entity entity = Reference.MINECRAFT_SERVER.getOverworld().getEntity(value);
                    if (entity != null) {
                        addEnemyEntity(entity);
                        iterator.remove();
                    }
                }
            }
        }
    }

    @Override
    public void unloadEntity(Entity entity) {
        if (memberEntities.contains(entity)) {
            removeMemberEntity(entity);
            int i = 0;
            boolean saved = false;
            if (!unprocessedData.containsKey("UnprocessedMemberEntity" + i)) {
                unprocessedData.put("UnprocessedMemberEntity" + i, entity.getUuid());
                saved = true;
            }
            else {
                while (unprocessedData.containsKey("UnprocessedMemberEntity" + i) && !saved) {
                    i++;
                    if (!unprocessedData.containsKey("UnprocessedMemberEntity" + i)) {
                        unprocessedData.put("UnprocessedMemberEntity" + i, entity.getUuid());
                        saved = true;
                    }
                }
            }
        } else if (friendEntities.contains(entity)) {
            removeFriendEntity(entity);
            int i = 0;
            boolean saved = false;
            if (!unprocessedData.containsKey("UnprocessedFriendEntity" + i)) {
                unprocessedData.put("UnprocessedFriendEntity" + i, entity.getUuid());
                saved = true;
            }
            else {
                while (unprocessedData.containsKey("UnprocessedFriendEntity" + i) && !saved) {
                    i++;
                    if (!unprocessedData.containsKey("UnprocessedFriendEntity" + i)) {
                        unprocessedData.put("UnprocessedFriendEntity" + i, entity.getUuid());
                        saved = true;
                    }
                }
            }
        } else if (enemyEntities.contains(entity)) {
            removeEnemyEntity(entity);
            int i = 0;
            boolean saved = false;
            if (!unprocessedData.containsKey("UnprocessedEnemyEntity" + i)) {
                unprocessedData.put("UnprocessedEnemyEntity" + i, entity.getUuid());
                saved = true;
            }
            else {
                while (unprocessedData.containsKey("UnprocessedEnemyEntity" + i) && !saved) {
                    i++;
                    if (!unprocessedData.containsKey("UnprocessedEnemyEntity" + i)) {
                        unprocessedData.put("UnprocessedEnemyEntity" + i, entity.getUuid());
                        saved = true;
                    }
                }
            }
        }
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
        int j = 0;
        i = 0;
        while (tag.contains("MemberEntity" + i)) {
            if (Reference.MINECRAFT_SERVER == null || Reference.MINECRAFT_SERVER.isLoading()) {
                unprocessedData.put("UnprocessedMemberEntity" + j++, tag.getUuid("MemberEntity" + i));
            } else {
                Entity entity = Reference.MINECRAFT_SERVER.getOverworld().getEntity(tag.getUuid("MemberEntity" + i));
                if (entity != null) {
                    addMemberEntity(entity);
                } else {
                    unprocessedData.put("UnprocessedMemberEntity" + j++, tag.getUuid("MemberEntity" + i));
                }
            }
            i++;
        }

        i = 0;
        j = 0;
        while (tag.contains("FriendEntity" + i)) {
            if (Reference.MINECRAFT_SERVER == null || Reference.MINECRAFT_SERVER.isLoading()) {
                unprocessedData.put("UnprocessedFriendEntity" + j++, tag.getUuid("FriendEntity" + i));
            } else {
                Entity entity = Reference.MINECRAFT_SERVER.getOverworld().getEntity(tag.getUuid("FriendEntity" + i));
                if (entity != null) {
                    addFriendEntity(entity);
                } else {
                    unprocessedData.put("UnprocessedFriendEntity" + j++, tag.getUuid("FriendEntity" + i));
                }
            }
            i++;
        }

        i = 0;
        j = 0;
        while (tag.contains("EnemyEntity" + i)) {
            if (Reference.MINECRAFT_SERVER == null || Reference.MINECRAFT_SERVER.isLoading()) {
                unprocessedData.put("UnprocessedEnemyEntity" + j++, tag.getUuid("EnemyEntity" + i));
            } else {
                Entity entity = Reference.MINECRAFT_SERVER.getOverworld().getEntity(tag.getUuid("EnemyEntity" + i));
                if (entity != null) {
                    addEnemyEntity(entity);
                } else {
                    unprocessedData.put("UnprocessedEnemyEntity" + j++, tag.getUuid("EnemyEntity" + i));
                }
            }
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

        memberEntities.forEach(mentity -> compound.putUuid("MemberEntity" + memberEntities.indexOf(mentity), mentity.getUuid()));
        friendEntities.forEach(fentity -> compound.putUuid("FriendEntity" + friendEntities.indexOf(fentity), fentity.getUuid()));
        enemyEntities.forEach(eentity -> compound.putUuid("EnemyEntity" + enemyEntities.indexOf(eentity), eentity.getUuid()));

        unprocessedData.forEach((key, value) -> {
            int i = 0;
            if (key.contains("Member")) {
                while (compound.contains("MemberEntity" + i)) {
                    i++;
                }

                compound.putUuid("MemberEntity" + i, value);
            }

            if (key.contains("Friend")) {
                while (compound.contains("FriendEntity" + i)) {
                    i++;
                }

                compound.putUuid("FriendEntity" + i, value);
            }

            if (key.contains("Enemy")) {
                while (compound.contains("EnemyEntity" + i)) {
                    i++;
                }

                compound.putUuid("EnemyEntity" + i, value);
            }
        });

        return compound;
    }

    @Override
    public String toString() {
        return "Faction: { " + getName() + " }";
    }
}

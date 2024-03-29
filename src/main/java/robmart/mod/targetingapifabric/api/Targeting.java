package robmart.mod.targetingapifabric.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.scoreboard.Team;
import robmart.mod.targetingapifabric.api.faction.Faction;

import java.util.*;

public class Targeting {

    private static final List<Faction> factionList = new ArrayList<>();

    /**
     * Gets an immutable shallow copy of the faction list
     */
    public static ImmutableList<Faction> getFactionList() {
        return ImmutableList.copyOf(factionList);
    }

    /**
     * Clears all factions. DO NOT use this unless you know the consequences
     */
    public static void clearFactions(){
        factionList.clear();
    }

    /**
     * Register a new faction
     * @param newFaction The faction that should be registered
     */
    public static boolean registerFaction(Faction newFaction){
        for (Faction faction : factionList) {
            if (faction.getName().equals(newFaction.getName()))
                return false;
        }
        factionList.add(newFaction);
        return true;
    }

    /**
     * Disband (remove) a faction
     * @param faction The faction that should be removed
     */
    public static void disbandFaction(Faction faction){
        if (factionList.removeIf(faction1 -> faction == faction1)) {
            faction.onDisband();
        }
    }

    /**
     * Gets faction from name
     * @param factionName The name of the requested faction
     * @return The requested faction
     */
    public static Faction getFaction(String factionName){
        for (Faction faction : factionList) {
            if (faction.getName().equals(factionName))
                return faction;
        }
        return null;
    }

    public static List<Faction> getFactionsFromEntity(Entity entity) {
        List<Faction> factionList = new ArrayList<>();
        for (Faction faction : Targeting.factionList) {
            if (faction.isMember(entity))
                factionList.add(faction);
        }
        return factionList;
    }

    /**
     * Checks if target is valid
     * @param type The target type used
     * @param caster The caster
     * @param target The target
     * @param excludeCaster Whether to exclude the caster. Can target caster if false
     * @return Whether the target is valid
     */
    public static boolean isValidTarget(TargetType type, Entity caster, Entity target, boolean excludeCaster) {
        if (caster == null || target == null) {
            return false;
        }
        if (!(target instanceof LivingEntity)){
            return false;
        }
        if (excludeCaster && caster.equals(target)) {
            return false;
        }
        // Targets should be alive
        if (!target.isAlive())
            return false;

        // Ignore spectators
        if (target instanceof PlayerEntity && target.isSpectator())
            return false;

        // Ignore Creative Mode players
        if (target instanceof PlayerEntity && ((PlayerEntity) target).isCreative())
            return false;

        return switch (type) {
            case ALL -> true;
            case SELF -> caster.equals(target);
            case PLAYERS -> target instanceof PlayerEntity;
            case FRIENDLY -> getTargetRelation(caster, target) == TargetRelationEnum.FRIEND;
            case ENEMY -> getTargetRelation(caster, target) == TargetRelationEnum.ENEMY;
            case NEUTRAL -> getTargetRelation(caster, target) == TargetRelationEnum.NEUTRAL;
        };
    }

    /**
     * Check if two entities are on the same scoreboard team
     * @param caster Entity one
     * @param target Entity two
     * @return Whether the two entities are on the same scoreboard team
     */
    public static boolean isSameTeam(Entity caster, Entity target) {
        Team myTeam = (Team) caster.getScoreboardTeam();
        Team otherTeam = (Team) target.getScoreboardTeam();
        return myTeam != null && otherTeam != null && myTeam.isEqual(otherTeam);
    }

    private static Entity getRootEntity(Entity source) {
        Entity controller = source.getPrimaryPassenger();
        if (controller != null) {
            return getRootEntity(controller);
        }

        if (source instanceof TameableEntity owned) {
            Entity owner = owned.getOwner();
            if (owner != null) {
                // Owner is online, so use it for relationship checks
                return getRootEntity(owner);
            } else if (owned.getOwner() != null) {
                // Entity is owned, but the owner is offline
                // If the owner is offline then there's not much we can do.
                return source;
            }
        }

        return source;
    }

    public static boolean areEntitiesEqual(Entity first, Entity second) {
        return first != null && second != null && first.getUuid().compareTo(second.getUuid()) == 0;
    }

    public static TargetRelationEnum getTargetRelation(Entity source, Entity target) {
        // can't be enemy with self
        if (areEntitiesEqual(source, target)) {
            return TargetRelationEnum.FRIEND;
        }

        if (!EntityPredicates.VALID_LIVING_ENTITY.test(target)) {
            return TargetRelationEnum.UNHANDLED;
        }

        //Same faction
        if (checkIfSameFaction(source, target) || isSameTeam(source, target))
            return TargetRelationEnum.FRIEND;

        // Friends
        for (Faction faction : factionList){
            if ((faction.isMember(source) && faction.isFriend(target)) || (faction.isMember(target) && faction.isFriend(source))){
                return TargetRelationEnum.FRIEND;
            }
        }

        //Enemy
        for (Faction faction : factionList) {
            if ((faction.isMember(source) && faction.isEnemy(target)) || (faction.isMember(target) && faction.isEnemy(source)))
                return TargetRelationEnum.ENEMY;
        }

        return TargetRelationEnum.NEUTRAL;
    }

    /**
     * Check if two entities are in the same faction
     * @param caster Entity one
     * @param target Entity two
     * @return Whether they are in the same faction or not
     */
    public static boolean checkIfSameFaction(Entity caster, Entity target){
        for (Faction faction : factionList){
            if (faction.isMember(target.getClass()) && faction.isMember(caster.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the two entities are part of the same faction, return that faction
     * @param caster Entity one
     * @param target Entity two
     * @return If they are part of the same faction, return it, otherwise returns null
     */
    public static Faction returnSameFaction(Entity caster, Entity target){
        for (Faction faction : factionList){
            if (faction.isMember(target.getClass()) && faction.isMember(caster.getClass())) {
                return faction;
            }
        }
        return null;
    }


    static boolean validCheck(Entity caster, Entity target, EnumSet<TargetRelationEnum> relations) {
        Entity casterRoot = getRootEntity(caster);
        Entity targetRoot = getRootEntity(target);

        TargetRelationEnum relation = getTargetRelation(casterRoot, targetRoot);
        return relations.contains(relation);
    }

    @Deprecated
    public static boolean isFriendly(Entity caster, Entity target) {
        return isValidFriendly(caster, target);
    }

    public static boolean isValidFriendly(Entity caster, Entity target) {
        return validCheck(caster, target, EnumSet.of(TargetRelationEnum.FRIEND));
    }

    public static boolean isValidEnemy(Entity caster, Entity target) {
        return validCheck(caster, target, EnumSet.of(TargetRelationEnum.ENEMY));
    }

    public static boolean isValidNeutral(Entity caster, Entity target) {
        return validCheck(caster, target, EnumSet.of(TargetRelationEnum.NEUTRAL, TargetRelationEnum.UNHANDLED));
    }

    /**
     * Check if caster has any relation to target
     * @param caster Caster
     * @param target Target
     * @return Whether they have any relation for not
     */
    public static boolean hasRelation(Entity caster, Entity target) {
        return !isValidNeutral(caster, target);
    }
}

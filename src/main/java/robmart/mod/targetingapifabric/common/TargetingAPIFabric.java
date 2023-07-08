package robmart.mod.targetingapifabric.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;
import robmart.mod.targetingapifabric.api.reference.Reference;
import robmart.mod.targetingapifabric.common.command.CommandFaction;

public class TargetingAPIFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register((server -> {
            Reference.MINECRAFT_SERVER = server;
            Faction animals = new Faction("FarmAnimals", false);
            animals.addFriendClass(PlayerEntity.class);
            animals.addMemberClass(CowEntity.class);
            animals.addMemberClass(SheepEntity.class);
            animals.addMemberClass(ChickenEntity.class);
            animals.addMemberClass(HorseEntity.class);
            animals.addMemberClass(LlamaEntity.class);
            animals.addMemberClass(DonkeyEntity.class);
            animals.addMemberClass(MuleEntity.class);
            animals.addMemberClass(PigEntity.class);
            animals.addMemberClass(ParrotEntity.class);
            animals.addMemberClass(RabbitEntity.class);
            animals.addMemberClass(OcelotEntity.class);
            animals.addMemberClass(WolfEntity.class);
            animals.addMemberClass(SquidEntity.class);
            animals.addMemberClass(MooshroomEntity.class);
            Targeting.registerFaction(animals);
            Faction animals2 = new Faction("FarmAnimals2", true);
            animals2.addFriendClass(PlayerEntity.class);
            animals2.addMemberClass(CowEntity.class);
            animals2.addMemberClass(SheepEntity.class);
            animals2.addMemberClass(ChickenEntity.class);
            animals2.addMemberClass(HorseEntity.class);
            animals2.addMemberClass(LlamaEntity.class);
            animals2.addMemberClass(DonkeyEntity.class);
            animals2.addMemberClass(MuleEntity.class);
            animals2.addMemberClass(PigEntity.class);
            animals2.addMemberClass(ParrotEntity.class);
            animals2.addMemberClass(RabbitEntity.class);
            animals2.addMemberClass(OcelotEntity.class);
            animals2.addMemberClass(WolfEntity.class);
            animals2.addMemberClass(SquidEntity.class);
            animals2.addMemberClass(MooshroomEntity.class);
            Targeting.registerFaction(animals2);
        }));

        ServerLifecycleEvents.SERVER_STOPPED.register((server -> {
            Targeting.clearFactions();
            Reference.MINECRAFT_SERVER = null;
        }));

        ServerEntityEvents.ENTITY_LOAD.register(((entity, world) -> {
            if (entity instanceof PlayerEntity) {
                Targeting.getFactionList().forEach(Faction::refreshPlayers);
            }
        }));

        ServerEntityEvents.ENTITY_UNLOAD.register(((entity, world) -> {
            Targeting.getFactionList().forEach(faction -> faction.unloadEntity(entity));
        }));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandFaction.register(dispatcher));
    }
}

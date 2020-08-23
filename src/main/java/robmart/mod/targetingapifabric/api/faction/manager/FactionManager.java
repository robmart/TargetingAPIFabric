package robmart.mod.targetingapifabric.api.faction.manager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.level.LevelProperties;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;

public class FactionManager implements IFactionManager {
    WorldProperties level;

    public FactionManager(WorldProperties level) {
        this.level = level;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        tag.getKeys().forEach(key -> {
            CompoundTag tag2 = (CompoundTag) tag.get(key);
            Faction faction = new Faction(tag2.getString("Name"), true);
            Targeting.registerFaction(faction);
            faction.readFromNbt(tag2);
        });
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        Targeting.getFactionMap().forEach((s, faction) -> tag.put(s, faction.writeToNbt(new CompoundTag())));
    }
}

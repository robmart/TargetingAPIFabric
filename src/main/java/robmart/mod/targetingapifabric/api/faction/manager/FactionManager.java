package robmart.mod.targetingapifabric.api.faction.manager;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.WorldProperties;
import robmart.mod.targetingapifabric.api.Targeting;
import robmart.mod.targetingapifabric.api.faction.Faction;

public class FactionManager implements IFactionManager {
    WorldProperties level;

    public FactionManager(WorldProperties level) {
        this.level = level;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        tag.getKeys().forEach(key -> {
            NbtCompound tag2 = (NbtCompound) tag.get(key);
            Faction faction;
            try {
                faction = (Faction) Class.forName(tag2.getString("Class")).getConstructor(String.class).newInstance(tag2.getString("Name"));
                if (Targeting.getFaction(faction.getName()) == null) {
                    Targeting.registerFaction(faction);
                    faction.readFromNbt(tag2);
                } else if (Targeting.getFaction(faction.getName()) != null) {
                    Targeting.getFaction(faction.getName()).readFromNbt(tag2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        Targeting.getFactionList().forEach((faction) -> {
            if (faction.getIsPermanent())
                tag.put(faction.getName(), faction.writeToNbt(new NbtCompound()));
        });
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        NbtCompound tag = buf.readNbt();
        if (tag != null) {
            this.readFromNbt(tag);
        }
    }
}

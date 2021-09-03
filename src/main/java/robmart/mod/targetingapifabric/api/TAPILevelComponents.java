package robmart.mod.targetingapifabric.api;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.level.LevelComponentInitializer;
import net.minecraft.util.Identifier;
import robmart.mod.targetingapifabric.api.faction.manager.FactionManager;
import robmart.mod.targetingapifabric.api.faction.manager.IFactionManager;

public class TAPILevelComponents implements LevelComponentInitializer {
    /**
    * You are never supposed to use this
     */
    public static final ComponentKey<IFactionManager> FACTION_MANAGER =
            ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("targetingapifabric:faction"), IFactionManager.class);

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        registry.register(FACTION_MANAGER, FactionManager::new);
    }
}

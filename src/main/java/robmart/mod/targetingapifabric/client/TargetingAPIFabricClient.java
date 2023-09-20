package robmart.mod.targetingapifabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import robmart.mod.targetingapifabric.api.TargetingClient;
import robmart.mod.targetingapifabric.api.event.ClientStatusEvents;

@Environment(EnvType.CLIENT)
public class TargetingAPIFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientStatusEvents.DISCONNECT_CLIENT_EVENT.register((client) -> TargetingClient.clearFactions());
    }
}

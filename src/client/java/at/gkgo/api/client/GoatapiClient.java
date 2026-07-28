package at.gkgo.api.client;


import at.gkgo.api.packet.GoatApiMetaPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class GoatapiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientConfigurationNetworking.registerGlobalReceiver(GoatApiMetaPacket.TYPE, (payload, context) -> {});
		ClientPlayNetworking.registerGlobalReceiver(GoatApiMetaPacket.TYPE, (payload,context) -> {});
	}
}
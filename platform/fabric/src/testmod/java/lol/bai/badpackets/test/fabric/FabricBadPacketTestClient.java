package lol.bai.badpackets.test.fabric;

import lol.bai.badpackets.test.BadPacketTest;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;

public class FabricBadPacketTestClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BadPacketTest.client();

        ClientConfigurationNetworking.registerGlobalReceiver(FabricBadPacketTest.FABRIC_TEST, (client, handler, buf, responseSender) -> {});
    }

}

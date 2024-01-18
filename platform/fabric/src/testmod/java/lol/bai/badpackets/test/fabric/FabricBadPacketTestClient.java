package lol.bai.badpackets.test.fabric;

import lol.bai.badpackets.test.BadPacketTest;
import net.fabricmc.api.ClientModInitializer;

public class FabricBadPacketTestClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BadPacketTest.client();

        // TODO
        // ClientConfigurationNetworking.registerGlobalReceiver(FabricBadPacketTest.FABRIC_TEST, (client, handler, buf, responseSender) -> {});
    }

}

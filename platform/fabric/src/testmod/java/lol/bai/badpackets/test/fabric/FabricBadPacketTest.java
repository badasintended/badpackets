package lol.bai.badpackets.test.fabric;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.test.BadPacketTest;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class FabricBadPacketTest implements ModInitializer {

    public static final ResourceLocation FABRIC_TEST = Constants.id("test/fabric");

    @Override
    public void onInitialize() {
        BadPacketTest.server();

        // TODO
        // ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
        //     if (!ServerConfigurationNetworking.canSend(handler, FABRIC_TEST)) {
        //         handler.disconnect(Component.literal("fabric test channel not found"));
        //     }
        // });
    }

}

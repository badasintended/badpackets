package lol.bai.badpackets.test.fabric;

import lol.bai.badpackets.test.BadPacketTest;
import net.fabricmc.api.ModInitializer;

public class FabricBadPacketTest implements ModInitializer {

    @Override
    public void onInitialize() {
        BadPacketTest.server();
    }

}

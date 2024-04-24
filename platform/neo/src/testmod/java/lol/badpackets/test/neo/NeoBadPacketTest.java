package lol.badpackets.test.neo;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.test.BadPacketTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = Bus.MOD)
public class NeoBadPacketTest {

    @SubscribeEvent
    static void setup(FMLCommonSetupEvent event) {
        BadPacketTest.common();
    }

}

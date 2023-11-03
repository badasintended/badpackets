package lol.badpackets.test.neo;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.test.BadPacketTest;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ForgeBadPacketTestClient {

    @SubscribeEvent
    static void setup(FMLClientSetupEvent event) {
        BadPacketTest.client();
    }

}

package lol.badpackets.test.neo;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.test.BadPacketTest;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ForgeBadPacketTestClient {

    @SubscribeEvent
    static void setup(FMLClientSetupEvent event) {
        BadPacketTest.client();
    }

}

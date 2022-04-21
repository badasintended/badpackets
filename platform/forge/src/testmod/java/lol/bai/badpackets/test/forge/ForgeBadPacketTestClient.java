package lol.bai.badpackets.test.forge;

import lol.bai.badpackets.test.BadPacketTest;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = ForgeBadPacketTest.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ForgeBadPacketTestClient {

    @SubscribeEvent
    static void setup(FMLClientSetupEvent event) {
        BadPacketTest.client();
    }

}

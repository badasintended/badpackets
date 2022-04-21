package lol.bai.badpackets.test.forge;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.test.BadPacketTest;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(ForgeBadPacketTest.MOD_ID)
@EventBusSubscriber(modid = ForgeBadPacketTest.MOD_ID, bus = Bus.MOD)
public class ForgeBadPacketTest {

    public static final String MOD_ID = Constants.MOD_ID + "_testmod";

    @SubscribeEvent
    static void setup(FMLCommonSetupEvent event) {
        BadPacketTest.server();
    }

}

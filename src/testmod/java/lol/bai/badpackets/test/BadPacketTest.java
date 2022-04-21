package lol.bai.badpackets.test;

import io.netty.buffer.Unpooled;
import lol.bai.badpackets.api.C2SPacketReceiver;
import lol.bai.badpackets.api.S2CPacketReceiver;
import lol.bai.badpackets.api.event.PacketSenderReadyCallback;
import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BadPacketTest {

    public static final ResourceLocation C2S = Constants.id("test/c2s");
    public static final ResourceLocation S2C = Constants.id("test/s2c");

    public static final Logger LOGGER = LogManager.getLogger(BadPacketTest.class);

    public static void server() {
        C2SPacketReceiver.register(C2S, (server, player, handler, buf, responseSender) ->
            server.execute(() -> LOGGER.info(C2S)));

        PacketSenderReadyCallback.registerServer((handler, sender, server) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.S2C));
            sender.send(BadPacketTest.S2C, new FriendlyByteBuf(Unpooled.buffer()));
        });
    }

    public static void client() {
        S2CPacketReceiver.register(S2C, (client, handler, buf, responseSender) ->
            client.execute(() -> LOGGER.info(S2C)));

        PacketSenderReadyCallback.registerClient((handler, sender, client) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.C2S));
            sender.send(BadPacketTest.C2S, new FriendlyByteBuf(Unpooled.buffer()));
        });
    }

}

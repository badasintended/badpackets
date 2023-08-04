package lol.bai.badpackets.test;

import io.netty.buffer.Unpooled;
import lol.bai.badpackets.api.PacketReceiver;
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
        PacketReceiver.registerC2S(C2S, (server, player, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        PacketReceiver.registerC2S(TestPayload.ID, TestPayload::new, (server, player, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        PacketSenderReadyCallback.registerServer((handler, sender, server) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.S2C));
            Validate.isTrue(sender.canSend(TestPayload.ID));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[untyped] server -> client");
            sender.send(BadPacketTest.S2C, buf);

            sender.send(new TestPayload("[typed] server -> client"));
        });
    }

    public static void client() {
        PacketReceiver.registerS2C(S2C, (client, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        PacketReceiver.registerS2C(TestPayload.ID, TestPayload::new, (client, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        PacketSenderReadyCallback.registerClient((handler, sender, client) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.C2S));
            Validate.isTrue(sender.canSend(TestPayload.ID));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[untyped] client -> server");
            sender.send(BadPacketTest.C2S, buf);

            sender.send(new TestPayload("[typed] client -> server"));
        });
    }

}

package lol.bai.badpackets.test;

import io.netty.buffer.Unpooled;
import lol.bai.badpackets.api.config.ConfigPackets;
import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BadPacketTest {

    public static final ResourceLocation CONFIG_C2S = Constants.id("test/config/c2s");
    public static final ResourceLocation CONFIG_S2C = Constants.id("test/config/s2c");

    public static final ResourceLocation PLAY_C2S = Constants.id("test/play/c2s");
    public static final ResourceLocation PLAY_S2C = Constants.id("test/play/s2c");

    public static final Logger LOGGER = LogManager.getLogger(BadPacketTest.class);

    public static void server() {
        ConfigPackets.registerServerReceiver(CONFIG_C2S, (server, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        ConfigPackets.registerServerReceiver(TestConfigPayload.ID, TestConfigPayload::new, (server, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        ConfigPackets.registerServerReadyCallback((handler, sender, server) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.CONFIG_S2C));
            Validate.isTrue(sender.canSend(TestConfigPayload.ID));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[config untyped] server -> client");
            sender.send(BadPacketTest.CONFIG_S2C, buf);

            sender.send(new TestConfigPayload("[config typed] server -> client"));
        });

        PlayPackets.registerServerReceiver(PLAY_C2S, (server, player, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        PlayPackets.registerServerReceiver(TestPlayPayload.ID, TestPlayPayload::new, (server, player, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        PlayPackets.registerServerReadyCallback((handler, sender, server) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.PLAY_S2C));
            Validate.isTrue(sender.canSend(TestPlayPayload.ID));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[play untyped] server -> client");
            sender.send(BadPacketTest.PLAY_S2C, buf);

            sender.send(new TestPlayPayload("[play typed] server -> client"));
        });
    }

    public static void client() {
        ConfigPackets.registerClientReceiver(CONFIG_S2C, (client, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        ConfigPackets.registerClientReceiver(TestConfigPayload.ID, TestConfigPayload::new, (client, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        ConfigPackets.registerClientReadyCallback((handler, sender, client) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.CONFIG_C2S));
            Validate.isTrue(sender.canSend(TestConfigPayload.ID));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[config untyped] client -> server");
            sender.send(BadPacketTest.CONFIG_C2S, buf);

            sender.send(new TestConfigPayload("[config typed] client -> server"));
        });

        PlayPackets.registerClientReceiver(PLAY_S2C, (client, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        PlayPackets.registerClientReceiver(TestPlayPayload.ID, TestPlayPayload::new, (client, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        PlayPackets.registerClientReadyCallback((handler, sender, client) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.PLAY_C2S));
            Validate.isTrue(sender.canSend(TestPlayPayload.ID));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[play untyped] client -> server");
            sender.send(BadPacketTest.PLAY_C2S, buf);

            sender.send(new TestPlayPayload("[play typed] client -> server"));
        });
    }

}

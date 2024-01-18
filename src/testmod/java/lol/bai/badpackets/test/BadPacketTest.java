package lol.bai.badpackets.test;

import io.netty.buffer.Unpooled;
import lol.bai.badpackets.api.config.ConfigPackets;
import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadPacketTest {

    public static final ResourceLocation CONFIG_TASK = Constants.id("test/config/task");

    public static final ResourceLocation CONFIG_C2S = Constants.id("test/config/c2s");
    public static final ResourceLocation CONFIG_S2C = Constants.id("test/config/s2c");

    public static final ResourceLocation PLAY_C2S = Constants.id("test/play/c2s");
    public static final ResourceLocation PLAY_S2C = Constants.id("test/play/s2c");

    public static final Logger LOGGER = LoggerFactory.getLogger(BadPacketTest.class);

    public static void server() {
        // TASK --------------------------------------------------------------------------------------------------------

        ConfigPackets.registerTask(CONFIG_TASK, (handler, sender, server) -> {
            if (sender.canSend(TestTaskPayload.TYPE)) {
                sender.send(new TestTaskPayload(TestTaskPayload.Stage.QUESTION_1));
                return true;
            }

            return false;
        });

        ConfigPackets.registerServerReceiver(TestTaskPayload.TYPE, TestTaskPayload.CODEC, (server, handler, payload, responseSender, taskFinisher) -> {
            LOGGER.info("[config task] client -> server " + payload.stage().name());

            switch (payload.stage()) {
                case ANSWER_1 -> responseSender.send(new TestTaskPayload(TestTaskPayload.Stage.QUESTION_2));
                case ANSWER_2 -> responseSender.send(new TestTaskPayload(TestTaskPayload.Stage.QUESTION_3));
                case ANSWER_3 -> taskFinisher.finish(CONFIG_TASK);
                default -> handler.disconnect(Component.literal("invalid stage"));
            }
        });

        // CONFIG ------------------------------------------------------------------------------------------------------

        ConfigPackets.registerServerReceiver(CONFIG_C2S, (server, handler, buf, responseSender, taskFinisher) ->
            LOGGER.info(buf.readUtf()));

        ConfigPackets.registerServerReceiver(TestConfigPayload.TYPE, TestConfigPayload.CODEC, (server, handler, payload, responseSender, taskFinisher) ->
            LOGGER.info(payload.msg()));

        ConfigPackets.registerServerReadyCallback((handler, sender, server) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.CONFIG_S2C));
            Validate.isTrue(sender.canSend(TestConfigPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[config untyped] server -> client");
            sender.send(BadPacketTest.CONFIG_S2C, buf);

            sender.send(new TestConfigPayload("[config typed] server -> client"));
        });

        // PLAY --------------------------------------------------------------------------------------------------------

        PlayPackets.registerServerReceiver(PLAY_C2S, (server, player, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        PlayPackets.registerServerReceiver(TestPlayPayload.TYPE, TestPlayPayload.CODEC, (server, player, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        PlayPackets.registerServerReadyCallback((handler, sender, server) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.PLAY_S2C));
            Validate.isTrue(sender.canSend(TestPlayPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[play untyped] server -> client");
            sender.send(BadPacketTest.PLAY_S2C, buf);

            sender.send(new TestPlayPayload("[play typed] server -> client"));
        });
    }

    public static void client() {
        // TASK --------------------------------------------------------------------------------------------------------

        ConfigPackets.registerClientReceiver(TestTaskPayload.TYPE, TestTaskPayload.CODEC, (client, handler, payload, responseSender) -> {
            LOGGER.info("[config task] server -> client " + payload.stage().name());

            switch (payload.stage()) {
                case QUESTION_1 -> responseSender.send(new TestTaskPayload(TestTaskPayload.Stage.ANSWER_1));
                case QUESTION_2 -> responseSender.send(new TestTaskPayload(TestTaskPayload.Stage.ANSWER_2));
                case QUESTION_3 -> responseSender.send(new TestTaskPayload(TestTaskPayload.Stage.ANSWER_3));
                default -> ConfigPackets.disconnect(handler, Component.literal("invalid stage"));
            }
        });

        // CONFIG ------------------------------------------------------------------------------------------------------

        ConfigPackets.registerClientReceiver(CONFIG_S2C, (client, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        ConfigPackets.registerClientReceiver(TestConfigPayload.TYPE, TestConfigPayload.CODEC, (client, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        ConfigPackets.registerClientReadyCallback((handler, sender, client) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.CONFIG_C2S));
            Validate.isTrue(sender.canSend(TestConfigPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[config untyped] client -> server");
            sender.send(BadPacketTest.CONFIG_C2S, buf);

            sender.send(new TestConfigPayload("[config typed] client -> server"));
        });

        // PLAY --------------------------------------------------------------------------------------------------------

        PlayPackets.registerClientReceiver(PLAY_S2C, (client, handler, buf, responseSender) ->
            LOGGER.info(buf.readUtf()));

        PlayPackets.registerClientReceiver(TestPlayPayload.TYPE, TestPlayPayload.CODEC, (client, handler, payload, responseSender) ->
            LOGGER.info(payload.msg()));

        PlayPackets.registerClientReadyCallback((handler, sender, client) -> {
            Validate.isTrue(sender.canSend(BadPacketTest.PLAY_C2S));
            Validate.isTrue(sender.canSend(TestPlayPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[play untyped] client -> server");
            sender.send(BadPacketTest.PLAY_C2S, buf);

            sender.send(new TestPlayPayload("[play typed] client -> server"));
        });
    }

}

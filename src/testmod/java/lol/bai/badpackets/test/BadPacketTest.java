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

        ConfigPackets.registerTask(CONFIG_TASK, context -> {
            if (context.canSend(TestTaskPayload.TYPE)) {
                context.send(new TestTaskPayload(TestTaskPayload.Stage.QUESTION_1));
                return true;
            }

            return false;
        });

        ConfigPackets.registerServerChannel(TestTaskPayload.TYPE, TestTaskPayload.CODEC, (context, payload) -> {
            LOGGER.info("[config task] client -> server " + payload.stage().name());

            switch (payload.stage()) {
                case ANSWER_1 -> context.send(new TestTaskPayload(TestTaskPayload.Stage.QUESTION_2));
                case ANSWER_2 -> context.send(new TestTaskPayload(TestTaskPayload.Stage.QUESTION_3));
                case ANSWER_3 -> context.finishTask(CONFIG_TASK);
                default -> context.handler().disconnect(Component.literal("invalid stage"));
            }
        });

        ConfigPackets.registerClientChannel(TestTaskPayload.TYPE, TestTaskPayload.CODEC);

        // CONFIG ------------------------------------------------------------------------------------------------------

        ConfigPackets.registerServerChannel(CONFIG_C2S, (context, buf) ->
            LOGGER.info(buf.readUtf()));

        ConfigPackets.registerServerChannel(TestConfigPayload.TYPE, TestConfigPayload.CODEC, (context, payload) ->
            LOGGER.info(payload.msg()));

        ConfigPackets.registerServerReadyCallback(context -> {
            Validate.isTrue(context.canSend(BadPacketTest.CONFIG_S2C));
            Validate.isTrue(context.canSend(TestConfigPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[config untyped] server -> client");
            context.send(BadPacketTest.CONFIG_S2C, buf);

            context.send(new TestConfigPayload("[config typed] server -> client"));
        });

        ConfigPackets.registerClientChannel(CONFIG_S2C);
        ConfigPackets.registerClientChannel(TestConfigPayload.TYPE, TestConfigPayload.CODEC);

        // PLAY --------------------------------------------------------------------------------------------------------

        PlayPackets.registerServerChannel(PLAY_C2S, (context, buf) ->
            LOGGER.info(buf.readUtf()));

        PlayPackets.registerServerChannel(TestPlayPayload.TYPE, TestPlayPayload.CODEC, (context, payload) ->
            LOGGER.info(payload.msg()));

        PlayPackets.registerServerReadyCallback(context -> {
            Validate.isTrue(context.canSend(BadPacketTest.PLAY_S2C));
            Validate.isTrue(context.canSend(TestPlayPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[play untyped] server -> client");
            context.send(BadPacketTest.PLAY_S2C, buf);

            context.send(new TestPlayPayload("[play typed] server -> client"));
        });

        PlayPackets.registerClientChannel(PLAY_S2C);
        PlayPackets.registerClientChannel(TestPlayPayload.TYPE, TestPlayPayload.CODEC);
    }

    public static void client() {
        // TASK --------------------------------------------------------------------------------------------------------

        ConfigPackets.registerClientReceiver(TestTaskPayload.TYPE, (context, payload) -> {
            LOGGER.info("[config task] server -> client " + payload.stage().name());

            switch (payload.stage()) {
                case QUESTION_1 -> context.send(new TestTaskPayload(TestTaskPayload.Stage.ANSWER_1));
                case QUESTION_2 -> context.send(new TestTaskPayload(TestTaskPayload.Stage.ANSWER_2));
                case QUESTION_3 -> context.send(new TestTaskPayload(TestTaskPayload.Stage.ANSWER_3));
                default -> context.disconnect(Component.literal("invalid stage"));
            }
        });

        // CONFIG ------------------------------------------------------------------------------------------------------

        ConfigPackets.registerClientReceiver(CONFIG_S2C, (context, buf) ->
            LOGGER.info(buf.readUtf()));

        ConfigPackets.registerClientReceiver(TestConfigPayload.TYPE, (context, payload) ->
            LOGGER.info(payload.msg()));

        ConfigPackets.registerClientReadyCallback(context -> {
            Validate.isTrue(context.canSend(BadPacketTest.CONFIG_C2S));
            Validate.isTrue(context.canSend(TestConfigPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[config untyped] client -> server");
            context.send(BadPacketTest.CONFIG_C2S, buf);

            context.send(new TestConfigPayload("[config typed] client -> server"));
        });

        // PLAY --------------------------------------------------------------------------------------------------------

        PlayPackets.registerClientReceiver(PLAY_S2C, (context, buf) ->
            LOGGER.info(buf.readUtf()));

        PlayPackets.registerClientReceiver(TestPlayPayload.TYPE, (context, payload) ->
            LOGGER.info(payload.msg()));

        PlayPackets.registerClientReadyCallback(context -> {
            Validate.isTrue(context.canSend(BadPacketTest.PLAY_C2S));
            Validate.isTrue(context.canSend(TestPlayPayload.TYPE));

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeUtf("[play untyped] client -> server");
            context.send(BadPacketTest.PLAY_C2S, buf);

            context.send(new TestPlayPayload("[play typed] client -> server"));
        });
    }

}

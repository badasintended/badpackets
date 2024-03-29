package lol.bai.badpackets.test;

import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record TestTaskPayload(
    Stage stage
) implements CustomPacketPayload {

    public static final Type<TestTaskPayload> TYPE = new Type<>(Constants.id("test/config/task/payload"));
    public static final StreamCodec<FriendlyByteBuf, TestTaskPayload> CODEC = CustomPacketPayload.codec(TestTaskPayload::write, TestTaskPayload::new);

    public enum Stage {
        QUESTION_1,
        QUESTION_2,
        QUESTION_3,
        ANSWER_1,
        ANSWER_2,
        ANSWER_3,
    }

    public TestTaskPayload(FriendlyByteBuf buf) {
        this(buf.readEnum(Stage.class));
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeEnum(stage);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}

package lol.bai.badpackets.test;

import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record TestTaskPayload(
    Stage stage
) implements CustomPacketPayload {

    public static final ResourceLocation ID = Constants.id("test/config/task/payload");

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

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeEnum(stage);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

}

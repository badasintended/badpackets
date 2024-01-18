package lol.bai.badpackets.test;

import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record TestConfigPayload(
    String msg
) implements CustomPacketPayload {

    public static final Type<TestConfigPayload> TYPE = new Type<>(Constants.id("test/config/payload"));
    public static final StreamCodec<FriendlyByteBuf, TestConfigPayload> CODEC = CustomPacketPayload.codec(TestConfigPayload::write, TestConfigPayload::new);

    public TestConfigPayload(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(msg);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}

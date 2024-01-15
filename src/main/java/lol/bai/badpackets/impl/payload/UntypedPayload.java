package lol.bai.badpackets.impl.payload;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record UntypedPayload(
    ResourceLocation channelId,
    FriendlyByteBuf buffer
) implements CustomPacketPayload {

    public static FriendlyByteBuf.Reader<UntypedPayload> reader(ResourceLocation channelId) {
        return buf -> {
            FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.buffer());
            copy.writeBytes(buf);
            return new UntypedPayload(channelId, copy);
        };
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeBytes(buffer.copy());
    }

    @Override
    public @NotNull ResourceLocation id() {
        return channelId;
    }

    @Override
    public FriendlyByteBuf buffer() {
        return new FriendlyByteBuf(buffer.copy());
    }

}

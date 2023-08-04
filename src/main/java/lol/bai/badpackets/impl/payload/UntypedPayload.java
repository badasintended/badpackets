package lol.bai.badpackets.impl.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record UntypedPayload(
    ResourceLocation channelId,
    FriendlyByteBuf buffer
) implements CustomPacketPayload {

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeBytes(buffer);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return channelId;
    }

}

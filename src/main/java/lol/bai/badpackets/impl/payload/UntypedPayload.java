package lol.bai.badpackets.impl.payload;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record UntypedPayload(
    Type<UntypedPayload> type,
    FriendlyByteBuf buffer
) implements CustomPacketPayload {

    private static final Map<ResourceLocation, Type<UntypedPayload>> TYPES = new HashMap<>();

    public static Type<UntypedPayload> type(ResourceLocation id) {
        return TYPES.computeIfAbsent(id, Type::new);
    }

    public UntypedPayload(ResourceLocation id, FriendlyByteBuf buf) {
        this(type(id), buf);
    }

    public static StreamCodec<FriendlyByteBuf, UntypedPayload> codec(ResourceLocation id) {
        return CustomPacketPayload.codec(UntypedPayload::write, buf -> {
            FriendlyByteBuf copy = new FriendlyByteBuf(Unpooled.buffer());
            copy.writeBytes(buf);
            return new UntypedPayload(id, copy);
        });
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeBytes(buffer.copy());
    }

    @Override
    public @NotNull Type<UntypedPayload> type() {
        return type;
    }

    @Override
    public FriendlyByteBuf buffer() {
        return new FriendlyByteBuf(buffer.copy());
    }

}

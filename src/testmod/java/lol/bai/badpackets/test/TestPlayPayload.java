package lol.bai.badpackets.test;

import lol.bai.badpackets.impl.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record TestPlayPayload(
    String msg
) implements CustomPacketPayload {

    public static final ResourceLocation ID = Constants.id("test/play/payload");

    public TestPlayPayload(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(msg);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

}

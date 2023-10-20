package lol.bai.badpackets.impl.mixin;

import java.util.Map;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundCustomPayloadPacket.class)
public interface AccessServerboundCustomPayloadPacket {

    @Accessor("KNOWN_TYPES")
    static Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> badpackets_getPacketReaders() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("KNOWN_TYPES")
    static void badpackets_setPacketReaders(Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> val) {
        throw new AssertionError();
    }

}

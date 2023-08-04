package lol.bai.badpackets.impl.mixin.client;

import java.util.Map;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundCustomPayloadPacket.class)
public interface AccessClientboundCustomPayloadPacket {

    @Accessor("KNOWN_TYPES")
    static Map<ResourceLocation, FriendlyByteBuf.Reader<? extends CustomPacketPayload>> badpackets_getPacketReaders() {
        throw new AssertionError();
    }

}

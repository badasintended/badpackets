package lol.bai.badpackets.api;

import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@ApiSide.ClientOnly
@FunctionalInterface
public interface S2CPacketReceiver {

    @ApiSide.ClientOnly
    static void register(ResourceLocation id, S2CPacketReceiver receiver) {
        ChannelRegistry.S2C.register(id, receiver);
    }

    void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender);

}

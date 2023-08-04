package lol.bai.badpackets.api;

import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Deprecated
@ApiSide.ClientOnly
@FunctionalInterface
public interface S2CPacketReceiver extends PacketReceiver.S2C<FriendlyByteBuf> {

    @ApiSide.ClientOnly
    static void register(ResourceLocation id, S2CPacketReceiver receiver) {
        PacketReceiver.registerS2C(id, receiver);
    }

    @Override
    void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender);

}

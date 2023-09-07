package lol.bai.badpackets.api;

import lol.bai.badpackets.api.play.ClientPlayPacketReceiver;
import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

@Deprecated(forRemoval = true)
@ApiSide.ClientOnly
@FunctionalInterface
public interface S2CPacketReceiver extends ClientPlayPacketReceiver<FriendlyByteBuf> {

    /**
     * @deprecated use {@link PlayPackets#registerClientReceiver(ResourceLocation, ClientPlayPacketReceiver)}
     */
    @ApiSide.ClientOnly
    @Deprecated(forRemoval = true)
    static void register(ResourceLocation id, S2CPacketReceiver receiver) {
        PlayPackets.registerClientReceiver(id, receiver);
    }

    @Override
    void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender);

}

package lol.bai.badpackets.api;

import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Deprecated(forRemoval = true)
@ApiSide.ServerOnly
@FunctionalInterface
public interface C2SPacketReceiver extends PlayPackets.ServerReceiver<FriendlyByteBuf> {

    /**
     * @deprecated use {@link PlayPackets#registerServerReceiver(ResourceLocation, PlayPackets.ServerReceiver)}
     */
    @Deprecated(forRemoval = true)
    static void register(ResourceLocation id, C2SPacketReceiver receiver) {
        PlayPackets.registerServerReceiver(id, receiver);
    }

    @Override
    void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender);

}

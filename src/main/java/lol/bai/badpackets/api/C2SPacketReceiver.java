package lol.bai.badpackets.api;

import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@ApiSide.ServerOnly
@FunctionalInterface
public interface C2SPacketReceiver {

    static void register(ResourceLocation id, C2SPacketReceiver receiver) {
        ChannelRegistry.C2S.register(id, receiver);
    }

    void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender);

}

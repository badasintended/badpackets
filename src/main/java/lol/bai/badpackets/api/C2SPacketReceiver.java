package lol.bai.badpackets.api;

import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Deprecated
@ApiSide.ServerOnly
@FunctionalInterface
public interface C2SPacketReceiver extends PacketReceiver.C2S<FriendlyByteBuf> {

    static void register(ResourceLocation id, C2SPacketReceiver receiver) {
        PacketReceiver.registerC2S(id, receiver);
    }

    @Override
    void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender);

}

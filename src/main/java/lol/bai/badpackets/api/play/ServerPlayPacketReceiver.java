package lol.bai.badpackets.api.play;

import lol.bai.badpackets.api.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@FunctionalInterface
public interface ServerPlayPacketReceiver<P> {

    void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, P payload, PacketSender responseSender);

}

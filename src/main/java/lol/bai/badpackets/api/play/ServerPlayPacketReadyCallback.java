package lol.bai.badpackets.api.play;

import lol.bai.badpackets.api.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@FunctionalInterface
public interface ServerPlayPacketReadyCallback {

    void onReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server);

}

package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;

@ApiSide.ServerOnly
@FunctionalInterface
public interface ServerConfigPacketReadyCallback {

    void onConfig(ServerConfigurationPacketListenerImpl handler, PacketSender sender, MinecraftServer server);

}

package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;

@ApiSide.ClientOnly
@FunctionalInterface
public interface ClientConfigPacketReadyCallback {

    void onConfig(ClientConfigurationPacketListenerImpl handler, PacketSender sender, Minecraft client);

}

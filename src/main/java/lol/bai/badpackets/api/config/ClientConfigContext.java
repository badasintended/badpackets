package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

@ApiSide.ClientOnly
@ApiStatus.NonExtendable
public interface ClientConfigContext extends PacketSender {

    Minecraft client();

    ClientConfigurationPacketListenerImpl handler();

    /**
     * Helper method to disconnect client-to-server connection.
     *
     * @param reason the disconnection reason
     */
    void disconnect(Component reason);

}

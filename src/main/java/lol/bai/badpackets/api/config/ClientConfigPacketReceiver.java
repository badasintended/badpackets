package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.resources.ResourceLocation;

@ApiSide.ClientOnly
@FunctionalInterface
public interface ClientConfigPacketReceiver<P> {

    /**
     * Server-to-client packet receiver.
     *
     * @param client         the client instance
     * @param handler        the connection handler instance
     * @param payload        the packet payload
     * @param responseSender the response packet sender, can only reliably send packet when in a
     *                       {@linkplain ConfigPackets#registerTask(ResourceLocation, ConfigTaskExecutor) task}
     */
    void receive(Minecraft client, ClientConfigurationPacketListenerImpl handler, P payload, PacketSender responseSender);

}

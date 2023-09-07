package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jetbrains.annotations.ApiStatus;

@FunctionalInterface
public interface ServerConfigPacketReceiver<P> {

    /**
     * Client-to-server packet receiver.
     *
     * @param server         the server instance
     * @param handler        the connection handler instance
     * @param payload        the packet payload
     * @param responseSender the response packet sender, can only reliably send packet when in a
     *                       {@linkplain ConfigPackets#registerTask(ResourceLocation, ConfigTaskExecutor) task}
     * @param taskFinisher   the task finisher
     */
    void receive(MinecraftServer server, ServerConfigurationPacketListenerImpl handler, P payload, PacketSender responseSender, TaskFinisher taskFinisher);

    @ApiStatus.NonExtendable
    interface TaskFinisher {

        void finish(ResourceLocation taskId);

    }

}

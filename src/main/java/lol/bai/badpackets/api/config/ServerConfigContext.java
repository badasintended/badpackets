package lol.bai.badpackets.api.config;

import lol.bai.badpackets.api.PacketSender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ServerConfigContext extends PacketSender {

    MinecraftServer server();

    ServerConfigurationPacketListenerImpl handler();

    void finishTask(ResourceLocation taskId);

}

package lol.bai.badpackets.api.play;

import lol.bai.badpackets.api.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ServerPlayConnectionContext extends PacketSender {

    MinecraftServer server();

    ServerPlayer player();

    ServerGamePacketListenerImpl handler();

}

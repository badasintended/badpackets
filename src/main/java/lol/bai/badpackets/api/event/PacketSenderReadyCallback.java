package lol.bai.badpackets.api.event;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.impl.marker.ApiSide;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Callback called after player joined the server.
 * This is the first point the {@link PacketSender}s are available.
 */
public final class PacketSenderReadyCallback {

    @ApiSide.ServerOnly
    public static void registerServer(Server callback) {
        CallbackRegistry.SERVER_PLAYER_JOIN.add(callback);
    }

    @ApiSide.ClientOnly
    public static void registerClient(Client callback) {
        CallbackRegistry.CLIENT_PLAYER_JOIN.add(callback);
    }

    @ApiSide.ServerOnly
    @FunctionalInterface
    public interface Server {

        void onJoin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server);

    }

    @ApiSide.ClientOnly
    @FunctionalInterface
    public interface Client {

        void onJoin(ClientPacketListener handler, PacketSender sender, Minecraft client);

    }

    private PacketSenderReadyCallback() {
    }

}

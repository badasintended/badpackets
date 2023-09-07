package lol.bai.badpackets.api.event;

import lol.bai.badpackets.api.PacketSender;
import lol.bai.badpackets.api.play.ClientPlayPacketReadyCallback;
import lol.bai.badpackets.api.play.PlayPackets;
import lol.bai.badpackets.api.play.ServerPlayPacketReadyCallback;
import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Deprecated(forRemoval = true)
public final class PacketSenderReadyCallback {

    /**
     * @deprecated use {@link PlayPackets#registerServerReadyCallback(ServerPlayPacketReadyCallback)}
     */
    @Deprecated(forRemoval = true)
    @ApiSide.ServerOnly
    public static void registerServer(Server callback) {
        PlayPackets.registerServerReadyCallback(callback);
    }

    /**
     * @deprecated use {@link PlayPackets#registerClientReadyCallback(ClientPlayPacketReadyCallback)}
     */
    @Deprecated(forRemoval = true)
    @ApiSide.ClientOnly
    public static void registerClient(Client callback) {
        PlayPackets.registerClientReadyCallback(callback);
    }

    @ApiSide.ServerOnly
    @FunctionalInterface
    public interface Server extends ServerPlayPacketReadyCallback {

        @Override
        default void onReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
            onJoin(handler, sender, server);
        }

        void onJoin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server);

    }

    @ApiSide.ClientOnly
    @FunctionalInterface
    public interface Client extends ClientPlayPacketReadyCallback {

        @Override
        default void onReady(ClientPacketListener handler, PacketSender sender, Minecraft client) {
            onJoin(handler, sender, client);
        }

        void onJoin(ClientPacketListener handler, PacketSender sender, Minecraft client);

    }

    private PacketSenderReadyCallback() {
    }

}

package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.PacketReceiver;
import lol.bai.badpackets.api.play.ServerPlayContext;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.Set;
import java.util.function.Consumer;

public class ServerPlayPacketHandler extends AbstractPacketHandler<ServerPlayContext, RegistryFriendlyByteBuf> implements ServerPlayContext {

    private final MinecraftServer server;
    private final ServerGamePacketListenerImpl handler;

    public ServerPlayPacketHandler(MinecraftServer server, ServerGamePacketListenerImpl handler, Connection connection) {
        super("ServerPlayPacketHandler for " + handler.getPlayer().getScoreboardName(), ChannelRegistry.PLAY_C2S, ClientboundCustomPayloadPacket::new, server, connection);
        this.server = server;
        this.handler = handler;
    }

    public static ServerPlayPacketHandler get(ServerPlayer player) {
        return ((PacketHandlerHolder<ServerPlayPacketHandler>) player.connection).badpackets_handler();
    }

    @Override
    protected Packet<?> createVanillaRegisterPacket(Set<ResourceLocation> channels, Consumer<RegistryFriendlyByteBuf> buf) {
        return PlatformProxy.INSTANCE.createVanillaRegisterPlayS2CPacket(server.registryAccess(), channels, buf);
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (var callback : CallbackRegistry.SERVER_PLAY) {
            callback.onReady(this);
        }
    }

    @Override
    protected void receiveUnsafe(PacketReceiver<ServerPlayContext, CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(this, payload);
    }

    @Override
    public MinecraftServer server() {
        return server;
    }

    @Override
    public ServerPlayer player() {
        return handler.player;
    }

    @Override
    public ServerGamePacketListenerImpl handler() {
        return handler;
    }

}

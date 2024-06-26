package lol.bai.badpackets.impl.handler;

import java.util.Set;
import java.util.function.Consumer;

import lol.bai.badpackets.api.PacketReceiver;
import lol.bai.badpackets.api.config.ClientConfigContext;
import lol.bai.badpackets.api.config.ConfigPackets;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ClientConfigPacketHandler extends AbstractPacketHandler<ClientConfigContext, FriendlyByteBuf> implements ClientConfigContext {

    private final Minecraft client;
    private final ClientConfigurationPacketListenerImpl listener;

    public ClientConfigPacketHandler(Minecraft client, ClientConfigurationPacketListenerImpl listener, Connection connection) {
        super("ClientConfigPacketHandler", ChannelRegistry.CONFIG_S2C, ServerboundCustomPayloadPacket::new, client, connection);

        this.client = client;
        this.listener = listener;
    }

    @Override
    protected Packet<?> createVanillaRegisterPacket(Set<ResourceLocation> channels, Consumer<FriendlyByteBuf> buf) {
        return PlatformProxy.INSTANCE.createVanillaRegisterConfigC2SPacket(channels, buf);
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (var callback : CallbackRegistry.CLIENT_READY_CONFIG) {
            callback.onReady(this);
        }

        sendInitialChannelSyncPacket();
    }

    @Override
    protected void receiveUnsafe(PacketReceiver<ClientConfigContext, CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(this, payload);
    }

    @Override
    public Minecraft client() {
        return client;
    }

    @Override
    public ClientConfigurationPacketListenerImpl handler() {
        return listener;
    }

    @Override
    public void disconnect(Component reason) {
        ConfigPackets.disconnect(listener, reason);
    }

}

package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.config.ClientConfigPacketReadyCallback;
import lol.bai.badpackets.api.config.ClientConfigPacketReceiver;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ClientConfigPacketHandler extends AbstractPacketHandler<ClientConfigPacketReceiver<CustomPacketPayload>> {

    private final Minecraft client;
    private final ClientConfigurationPacketListenerImpl listener;

    public ClientConfigPacketHandler(Minecraft client, ClientConfigurationPacketListenerImpl listener, Connection connection) {
        super("ClientConfigPacketHandler", ChannelRegistry.CONFIG_S2C, ServerboundCustomPayloadPacket::new, connection);

        this.client = client;
        this.listener = listener;
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (ClientConfigPacketReadyCallback callback : CallbackRegistry.CLIENT_READY_CONFIG) {
            callback.onConfig(listener, this, client);
        }

        sendInitialChannelSyncPacket();
    }

    @Override
    protected void receive(ClientConfigPacketReceiver<CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(client, listener, payload, this);
    }

}

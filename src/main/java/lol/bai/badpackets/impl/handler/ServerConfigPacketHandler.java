package lol.bai.badpackets.impl.handler;

import java.util.function.Consumer;

import lol.bai.badpackets.api.config.ConfigPackets;
import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jetbrains.annotations.NotNull;

public class ServerConfigPacketHandler extends AbstractPacketHandler<ConfigPackets.ServerReceiver<CustomPacketPayload>> {

    private final MinecraftServer server;
    private final ServerConfigurationPacketListenerImpl listener;

    public ServerConfigPacketHandler(MinecraftServer server, ServerConfigurationPacketListenerImpl listener, Connection connection) {
        super("ServerConfigPacketHandler for " + listener.getOwner().getName(), ChannelRegistry.CONFIG_C2S, ClientboundCustomPayloadPacket::new, connection);

        this.server = server;
        this.listener = listener;
    }

    public Task createTask() {
        return new Task();
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (ConfigPackets.ServerReadyCallback callback : CallbackRegistry.SERVER_READY_CONFIG) {
            callback.onConfig(listener, this, server);
        }

        ((TaskFinisher) listener).badpackets_finishTask(Task.TYPE);
    }

    @Override
    protected void receive(ConfigPackets.ServerReceiver<CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(server, listener, payload, this);
    }

    public interface TaskFinisher {

        void badpackets_finishTask(ConfigurationTask.Type type);

    }

    public class Task implements ConfigurationTask {

        public static final Type TYPE = new Type(Constants.MOD_ID);

        @Override
        public void start(@NotNull Consumer<Packet<?>> sender) {
            sendInitialChannelSyncPacket();
            sender.accept(new ClientboundPingPacket(Constants.PING_PONG));
        }

        @Override
        public @NotNull Type type() {
            return TYPE;
        }

    }

}

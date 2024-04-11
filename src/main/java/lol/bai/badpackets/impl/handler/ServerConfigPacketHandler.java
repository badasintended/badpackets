package lol.bai.badpackets.impl.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import lol.bai.badpackets.api.config.ConfigTaskExecutor;
import lol.bai.badpackets.api.config.ServerConfigPacketReadyCallback;
import lol.bai.badpackets.api.config.ServerConfigPacketReceiver;
import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import lol.bai.badpackets.impl.registry.CallbackRegistry;
import lol.bai.badpackets.impl.registry.ChannelRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jetbrains.annotations.NotNull;

public class ServerConfigPacketHandler extends AbstractPacketHandler<ServerConfigPacketReceiver<CustomPacketPayload>, FriendlyByteBuf> implements ServerConfigPacketReceiver.TaskFinisher {

    public static final Map<ResourceLocation, CustomTask> CUSTOM_TASKS = new HashMap<>();

    private final MinecraftServer server;
    private final ServerConfigurationPacketListenerImpl listener;

    public ServerConfigPacketHandler(MinecraftServer server, ServerConfigurationPacketListenerImpl listener, Connection connection) {
        super("ServerConfigPacketHandler for " + listener.getOwner().getName(), ChannelRegistry.CONFIG_C2S, ClientboundCustomPayloadPacket::new, server, connection);

        this.server = server;
        this.listener = listener;
    }

    public static void registerTask(ResourceLocation id, ConfigTaskExecutor executor) {
        CUSTOM_TASKS.put(id, new CustomTask(id, executor));
    }

    public CallbackTask createCallbackTask() {
        return new CallbackTask();
    }

    @Override
    protected Packet<?> createVanillaRegisterPacket(Set<ResourceLocation> channels, Consumer<FriendlyByteBuf> buf) {
        return PlatformProxy.INSTANCE.createVanillaRegisterConfigS2CPacket(channels, buf);
    }

    @Override
    protected void onInitialChannelSyncPacketReceived() {
        for (ServerConfigPacketReadyCallback callback : CallbackRegistry.SERVER_READY_CONFIG) {
            callback.onConfig(listener, this, server);
        }

        ((TaskFinisher) listener).badpackets_finishTask(CallbackTask.TYPE);
    }

    @Override
    protected void receiveUnsafe(ServerConfigPacketReceiver<CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(server, listener, payload, this, this);
    }

    @Override
    public void finish(ResourceLocation taskId) {
        ((TaskFinisher) listener).badpackets_finishTask(CUSTOM_TASKS.get(taskId).type);
    }

    public interface TaskFinisher {

        void badpackets_finishTask(ConfigurationTask.Type type);

    }

    public class CallbackTask implements ConfigurationTask {

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

    public static class CustomTask implements ConfigurationTask {

        private final Type type;
        private final ConfigTaskExecutor executor;

        private ServerConfigPacketHandler handler;

        public CustomTask(ResourceLocation id, ConfigTaskExecutor executor) {
            this.type = new Type(id.toString());
            this.executor = executor;
        }

        public void setHandler(ServerConfigPacketHandler handler) {
            this.handler = handler;
        }

        @Override
        public void start(@NotNull Consumer<Packet<?>> consumer) {
            if (!executor.runTask(handler.listener, handler, handler.server)) {
                ((TaskFinisher) handler.listener).badpackets_finishTask(type);
            }
        }

        @Override
        public @NotNull Type type() {
            return type;
        }

    }

}

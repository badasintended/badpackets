package lol.bai.badpackets.impl.handler;

import lol.bai.badpackets.api.PacketReceiver;
import lol.bai.badpackets.api.config.ConfigTaskExecutor;
import lol.bai.badpackets.api.config.ServerConfigContext;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ServerConfigPacketHandler extends AbstractPacketHandler<ServerConfigContext, FriendlyByteBuf> implements ServerConfigContext {

    public static final Map<ResourceLocation, CustomTask> CUSTOM_TASKS = new HashMap<>();

    private final MinecraftServer server;
    private final ServerConfigurationPacketListenerImpl listener;

    public ServerConfigPacketHandler(MinecraftServer server, ServerConfigurationPacketListenerImpl listener, Connection connection) {
        super("ServerConfigPacketHandler for " + listener.getOwner().name(), ChannelRegistry.CONFIG_C2S, ClientboundCustomPayloadPacket::new, server, connection);

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
        for (var callback : CallbackRegistry.SERVER_READY_CONFIG) {
            callback.onReady(this);
        }

        ((TaskFinisher) listener).badpackets_finishTask(CallbackTask.TYPE);
    }

    @Override
    protected void receiveUnsafe(PacketReceiver<ServerConfigContext, CustomPacketPayload> receiver, CustomPacketPayload payload) {
        receiver.receive(this, payload);
    }

    @Override
    public MinecraftServer server() {
        return server;
    }

    @Override
    public ServerConfigurationPacketListenerImpl handler() {
        return listener;
    }

    @Override
    public void finishTask(ResourceLocation taskId) {
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
            if (!executor.runTask(handler)) {
                ((TaskFinisher) handler.listener).badpackets_finishTask(type);
            }
        }

        @Override
        public @NotNull Type type() {
            return type;
        }

    }

}

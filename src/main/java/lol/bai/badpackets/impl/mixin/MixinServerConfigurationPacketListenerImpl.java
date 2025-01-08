package lol.bai.badpackets.impl.mixin;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.handler.PacketHandlerHolder;
import lol.bai.badpackets.impl.handler.ServerConfigPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Queue;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class MixinServerConfigurationPacketListenerImpl extends MixinServerCommonPacketListenerImpl implements ServerConfigPacketHandler.TaskFinisher, PacketHandlerHolder<ServerConfigPacketHandler> {

    @Shadow
    @Final
    private Queue<ConfigurationTask> configurationTasks;

    @Shadow
    protected abstract void finishCurrentTask(ConfigurationTask.Type type);

    @Shadow
    @Nullable
    private ConfigurationTask currentTask;

    @Unique
    private ServerConfigPacketHandler badpackets_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createPacketHandler(MinecraftServer server, Connection connection, CommonListenerCookie cookie, CallbackInfo ci) {
        badpackets_packetHandler = new ServerConfigPacketHandler(server, (ServerConfigurationPacketListenerImpl) (Object) this, connection);
    }

    @Inject(method = "startConfiguration", at = @At("HEAD"))
    private void badpackets_initPacketHandler(CallbackInfo ci) {
        configurationTasks.add(badpackets_packetHandler.createCallbackTask());
        configurationTasks.addAll(ServerConfigPacketHandler.CUSTOM_TASKS.values());
    }

    @Inject(method = "startNextTask", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/server/network/ServerConfigurationPacketListenerImpl;currentTask:Lnet/minecraft/server/network/ConfigurationTask;"))
    private void badpackets_attachCustomTaskContext(CallbackInfo ci, ConfigurationTask task) {
        if (task instanceof ServerConfigPacketHandler.CustomTask custom) {
            custom.setHandler(badpackets_packetHandler);
        }
    }

    @Inject(method = "finishCurrentTask", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/server/network/ServerConfigurationPacketListenerImpl;currentTask:Lnet/minecraft/server/network/ConfigurationTask;"))
    private void badpackets_detachCustomTaskContext(ConfigurationTask.Type type, CallbackInfo ci) {
        if (currentTask instanceof ServerConfigPacketHandler.CustomTask custom) {
            custom.setHandler(null);
        }
    }

    @Override
    public ServerConfigPacketHandler badpackets_handler() {
        return badpackets_packetHandler;
    }

    @Override
    protected boolean badpackets_handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        return badpackets_packetHandler.receive(packet.payload());
    }

    @Override
    protected void badpackets_onPong(int id) {
        if (id == Constants.PING_PONG) {
            finishCurrentTask(ServerConfigPacketHandler.CallbackTask.TYPE);
        }
    }

    @Override
    public void badpackets_finishTask(ConfigurationTask.Type type) {
        finishCurrentTask(type);
    }

}

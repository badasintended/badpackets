package lol.bai.badpackets.impl.mixin;

import java.util.Queue;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.handler.ServerConfigPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class MixinServerConfigurationPacketListenerImpl extends MixinServerCommonPacketListenerImpl implements ServerConfigPacketHandler.TaskFinisher {

    @Shadow
    @Final
    private Queue<ConfigurationTask> configurationTasks;

    @Shadow
    protected abstract void finishCurrentTask(ConfigurationTask.Type type);

    @Unique
    private ServerConfigPacketHandler badpackets_packetHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void badpackets_createPacketHandler(MinecraftServer server, Connection connection, CommonListenerCookie cookie, CallbackInfo ci) {
        badpackets_packetHandler = new ServerConfigPacketHandler(server, (ServerConfigurationPacketListenerImpl) (Object) this, connection);
    }

    @Inject(method = "startConfiguration", at = @At("HEAD"))
    private void badpackets_initPacketHandler(CallbackInfo ci) {
        configurationTasks.add(badpackets_packetHandler.createTask());
    }

    @Inject(method = "handleConfigurationFinished", at = @At("RETURN"))
    private void badpackets_removePacketHandler(ServerboundFinishConfigurationPacket packet, CallbackInfo ci) {
        badpackets_packetHandler.remove();
    }

    @Override
    protected void badpackets_removePacketHandler() {
        badpackets_packetHandler.remove();
    }

    @Override
    protected boolean badpackets_handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        PacketUtils.ensureRunningOnSameThread(packet, (ServerCommonPacketListener) this, server);
        return badpackets_packetHandler.receive(packet.payload());
    }

    @Override
    protected void badpackets_onPong(int id) {
        if (id == Constants.PING_PONG) {
            finishCurrentTask(ServerConfigPacketHandler.Task.TYPE);
        }
    }

    @Override
    public void badpackets_finishTask(ConfigurationTask.Type type) {
        finishCurrentTask(type);
    }

}

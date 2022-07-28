package lol.bai.badpackets.impl.mixin;

import io.netty.channel.ChannelFuture;
import lol.bai.badpackets.impl.handler.AbstractPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Connection.class)
public class MixinConnection {

    @ModifyVariable(method = "doSendPacket", at = @At(value = "INVOKE_ASSIGN", target = "Lio/netty/channel/Channel;writeAndFlush(Ljava/lang/Object;)Lio/netty/channel/ChannelFuture;"))
    private ChannelFuture addListener(ChannelFuture value, Packet<?> packet, @Nullable PacketSendListener listener) {
        if (listener instanceof AbstractPacketHandler.Listener listener1) {
            value.addListener(listener1.delegate);
        }

        return value;
    }

}

package lol.bai.badpackets.impl.mixin.client;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientCommonPacketListenerImpl.class)
public interface AccessClientCommonPacketListenerImpl {

    @Accessor("connection")
    Connection badpackets_connection();

}

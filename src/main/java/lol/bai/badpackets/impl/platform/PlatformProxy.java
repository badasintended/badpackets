package lol.bai.badpackets.impl.platform;

import java.util.ServiceLoader;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.payload.UntypedPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PlatformProxy {

    public static final PlatformProxy INSTANCE = ServiceLoader.load(PlatformProxy.class).findFirst().orElseGet(PlatformProxy::new);

    public CustomPacketPayload createVanillaRegisterPayload(FriendlyByteBuf buf) {
        return new UntypedPayload(Constants.CHANNEL_SYNC, buf);
    }

}

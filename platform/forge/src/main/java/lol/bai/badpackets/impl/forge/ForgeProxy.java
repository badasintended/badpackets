package lol.bai.badpackets.impl.forge;

import lol.bai.badpackets.impl.Constants;
import lol.bai.badpackets.impl.platform.PlatformProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;

public class ForgeProxy extends PlatformProxy {

    @Override
    public CustomPacketPayload createVanillaRegisterPayload(FriendlyByteBuf buf) {
        return new DiscardedPayload(Constants.MC_REGISTER_CHANNEL, buf);
    }

}

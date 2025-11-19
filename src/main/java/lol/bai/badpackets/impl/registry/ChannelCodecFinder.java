package lol.bai.badpackets.impl.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ChannelCodecFinder {

    static void attach(StreamCodec<?, ?> codec, ChannelCodecFinder finder) {
        ((Holder) codec).badpackets_setChannelCodecFinder(finder);
    }

    @Nullable
    StreamCodec<FriendlyByteBuf, CustomPacketPayload> getCodec(Identifier id, FriendlyByteBuf buf);

    interface Holder {

        void badpackets_setChannelCodecFinder(ChannelCodecFinder finder);

    }

}

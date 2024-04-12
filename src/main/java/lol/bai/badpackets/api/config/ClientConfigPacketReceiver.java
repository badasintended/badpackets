package lol.bai.badpackets.api.config;

import lol.bai.badpackets.impl.marker.ApiSide;
import net.minecraft.resources.ResourceLocation;

@ApiSide.ClientOnly
@FunctionalInterface
public interface ClientConfigPacketReceiver<P> {

    /**
     * Server-to-client packet receiver.
     * <p>
     * Can only reliably send packet when in a
     * {@linkplain ConfigPackets#registerTask(ResourceLocation, ConfigTaskExecutor) task}.
     */
    void receive(ClientConfigConnectionContext context, P payload);

}

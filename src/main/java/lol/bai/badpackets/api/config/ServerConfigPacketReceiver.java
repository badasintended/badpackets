package lol.bai.badpackets.api.config;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface ServerConfigPacketReceiver<P> {

    /**
     * Client-to-server packet receiver.
     * <p>
     * Can only reliably send packet when in a
     * {@linkplain ConfigPackets#registerTask(ResourceLocation, ConfigTaskExecutor) task}.
     */
    void receive(ServerConfigConnectionContext context, P payload);

}

package lol.bai.badpackets.api.config;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface ConfigTaskExecutor {

    /**
     * Return whether the task is run or not.
     * <p>
     * Make use of {@link ServerConfigContext#canSend(ResourceLocation)} to check whether the client can process the task,
     * and return {@code false} otherwise.
     * <p>
     * Once the client received the task packet, it should send a response packet to the server.
     * After the server received the response packet, it should call {@link ServerConfigContext#finishTask(ResourceLocation)}
     * to allow the client to join the server.
     */
    boolean runTask(ServerConfigContext context);

}

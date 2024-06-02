package lol.bai.badpackets.impl;

import net.minecraft.resources.ResourceLocation;

public class Constants {

    public static final String MOD_ID = "badpackets";

    public static final ResourceLocation CHANNEL_SYNC = id("channel_sync");
    public static final ResourceLocation MC_REGISTER_CHANNEL = ResourceLocation.parse("minecraft:register");
    public static final ResourceLocation MC_UNREGISTER_CHANNEL = ResourceLocation.parse("minecraft:unregister");

    public static final int PING_PONG = 0xBAD4C7_01;

    public static final byte CHANNEL_SYNC_SINGLE = 0;
    public static final byte CHANNEL_SYNC_INITIAL = 1;

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}

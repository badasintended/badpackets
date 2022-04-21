package lol.bai.badpackets.impl;

import net.minecraft.resources.ResourceLocation;

public class Constants {

    public static final String MOD_ID = "badpackets";
    public static final ResourceLocation CHANNEL_SYNC = new ResourceLocation(MOD_ID, "channel_sync");
    public static final byte CHANNEL_SYNC_SINGLE = 0;
    public static final byte CHANNEL_SYNC_INITIAL = 1;

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}

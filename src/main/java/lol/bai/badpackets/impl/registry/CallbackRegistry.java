package lol.bai.badpackets.impl.registry;

import java.util.ArrayList;

import lol.bai.badpackets.api.config.ConfigPackets;
import lol.bai.badpackets.api.play.PlayPackets;

public class CallbackRegistry {

    public static final ArrayList<ConfigPackets.ClientReadyCallback> CLIENT_READY_CONFIG = new ArrayList<>();
    public static final ArrayList<ConfigPackets.ServerReadyCallback> SERVER_READY_CONFIG = new ArrayList<>();

    public static final ArrayList<PlayPackets.ClientReadyCallback> CLIENT_PLAY = new ArrayList<>();
    public static final ArrayList<PlayPackets.ServerReadyCallback> SERVER_PLAY = new ArrayList<>();

}

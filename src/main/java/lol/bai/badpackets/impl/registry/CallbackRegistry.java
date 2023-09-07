package lol.bai.badpackets.impl.registry;

import java.util.ArrayList;

import lol.bai.badpackets.api.config.ClientConfigPacketReadyCallback;
import lol.bai.badpackets.api.config.ServerConfigPacketReadyCallback;
import lol.bai.badpackets.api.play.ClientPlayPacketReadyCallback;
import lol.bai.badpackets.api.play.ServerPlayPacketReadyCallback;

public class CallbackRegistry {

    public static final ArrayList<ClientConfigPacketReadyCallback> CLIENT_READY_CONFIG = new ArrayList<>();
    public static final ArrayList<ServerConfigPacketReadyCallback> SERVER_READY_CONFIG = new ArrayList<>();

    public static final ArrayList<ClientPlayPacketReadyCallback> CLIENT_PLAY = new ArrayList<>();
    public static final ArrayList<ServerPlayPacketReadyCallback> SERVER_PLAY = new ArrayList<>();

}

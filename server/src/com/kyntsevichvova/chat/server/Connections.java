package com.kyntsevichvova.chat.server;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by petuh on 2/10/2016.
 */
public class Connections {

    private ConcurrentHashMap<ClientConnection, String> connected;

    private Server server;

    public Connections(Server server) {
        this.server = server;
        this.connected = new ConcurrentHashMap<>();
    }

    public boolean isDisconnected(ClientConnection connection) {
        return !connected.containsKey(connection);
    }

    public void connect(ClientConnection connection, String name) {
        connected.put(connection, name);
    }

    public void disconnect(ClientConnection connection) {
        connected.remove(connection);
    }

    public void broadcast(KLON klon) {
        connected.forEach((key, value) -> key.sendMessage(klon));
    }
}

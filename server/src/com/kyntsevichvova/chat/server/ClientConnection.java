package com.kyntsevichvova.chat.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class ClientConnection implements Runnable {

    private static Map<Server, Set<ClientConnection>> allConnections = new HashMap<>();
    private Socket socket;
    private Server server;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Thread thread;
    private String login = null;

    public ClientConnection(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        this.objectInputStream = new ObjectInputStream(objectInputStream);
        this.objectOutputStream = new ObjectOutputStream(objectOutputStream);
        this.thread = new Thread(this);
    }

    public static void stopAll(Server server) {
        if (!allConnections.containsKey(server)) 
            allConnections.put(server, new HashSet<>());
        List<ClientConnection> list = new ArrayList<>(allConnections.get(server));
        list.forEach(ClientConnection::stop);
    }

    @Override
    public void run() {
        while (socket.isConnected() && !Thread.interrupted()) {
            try {
                HashMap<String, Object> map = (HashMap<String, Object>) objectInputStream.readObject();
                server.onMessage(map, this);
            } catch (SocketException | EOFException e) {
                break;
            } catch (IOException e) {
                ServerLogger.log("Exception was caught while reading message");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        server.disconnect(this);
        ServerLogger.log(String.format("Disconnected %s%n", socket));
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendMessage(HashMap<String, Object> map) {
        try {
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendError(String error) {
        this.sendMessage(Protocol.createErrorMessage(error));
    }

    @Override
    public String toString() {
        return this.socket.toString();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void start() {
        thread.start();
        if (!allConnections.containsKey(server))
            allConnections.put(server, new HashSet<>());
        allConnections.get(server).add(this);
    }

    public void stop() {
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!allConnections.containsKey(server))
            allConnections.put(server, new HashSet<>());
        allConnections.get(server).remove(this);
    }
}

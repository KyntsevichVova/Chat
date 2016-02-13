package com.kyntsevichvova.chat.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class ClientConnection implements Runnable {

    private final static int MAX_LENGTH = 8 * 1024 * 1024;

    private static Map<Server, Set<ClientConnection>> allConnections = new HashMap<>();
    private Socket socket;
    private Server server;
    private InputStream is;
    private OutputStream os;
    private Thread thread;
    private String login = null;

    public ClientConnection(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
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
                int length = is.read() << 24;
                length += is.read() << 16;
                length += is.read() << 8;
                length += is.read();
                if (length > MAX_LENGTH) { //skip
                    is.skip(length);
                    continue;
                }
                byte[] bytes = new byte[length];
                int actualLength = is.read(bytes);
                if (actualLength != length) break;
                server.onMessage(KLON.parseBytes(bytes), this);
            } catch (SocketException | EOFException e) {
                break;
            } catch (IOException e) {
                ServerLogger.log("Exception was caught while reading message");
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        server.disconnect(this);
        ServerLogger.log(String.format("Disconnected %s%n", socket));
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void sendMessage(KLON klon) {
        try {
            os.write(klon.toBytes());
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

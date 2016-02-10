package com.kyntsevichvova.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection implements Runnable {

    private Socket socket;
    private Server server;
    private DataInputStream dis;
    private DataOutputStream dos;

    private String login = null;

    public ClientConnection(Socket socket, Server server) throws IOException {
        this.server = server;
        this.socket = socket;
        dis = new DataInputStream(this.socket.getInputStream());
        dos = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        while (socket.isConnected() && !Thread.interrupted()) {
            try {
                String tmp = dis.readUTF();
                server.onMessage(tmp, this);
            } catch (SocketException e) {
                e.printStackTrace();
                break;
            } catch (IOException e) {
                ServerLogger.log("Exception was caught while reading message");
                e.printStackTrace();
            }
        }
        server.disconnect(this);
        ServerLogger.log(String.format("Disconnected %s%n", socket));
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendError(String error) {
        try {
            dos.writeUTF("/error " + error);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            dos.writeUTF("/message " + message);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}

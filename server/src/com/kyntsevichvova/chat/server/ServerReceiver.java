package com.kyntsevichvova.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerReceiver implements Runnable {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ServerReceiver(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(this.socket.getInputStream());
        dos = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String tmp = dis.readUTF();
                String arg = "", message = "";
                for (int i = 0; i < tmp.length() && tmp.charAt(i) != ' '; i++) {
                    arg += tmp.charAt(i);
                }
                for (int i = arg.length() + 1; i < tmp.length(); i++) {
                    message += tmp.charAt(i);
                }
                if (Server.isDisconnected(socket) && arg.startsWith("/message")) {
                    new Thread(new ServerSender("error", "You're not logged in", socket)).start();
                    continue;
                }
                if (arg.startsWith("/message")) {
                    new Thread(new ServerLogger(String.format("New message from %s%n%s", socket, message))).start();
                    new Thread(new ServerSender("message", message, socket)).start();
                }
                if (arg.startsWith("/register")) {
                    new Thread(new Register(message, socket)).start();
                }
                if (arg.startsWith("/login")) {
                    new Thread(new Loginer(socket, message)).start();
                }
            } catch (IOException e) {
                new ServerLogger("Exception was caught while reading message%n");
            }
        }
        new ServerLogger(String.format("Disconnected %s%n", socket));
        Server.disconnect(socket);
    }
}

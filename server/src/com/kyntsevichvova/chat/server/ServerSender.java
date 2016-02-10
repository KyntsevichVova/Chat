package com.kyntsevichvova.chat.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ServerSender implements Runnable {

    private String arg;
    private String message;
    private Socket socket;
    private Map<Socket, String> receivers;
    private String date;
    private String senderLogin;

    public ServerSender(String arg, String message, Socket socket) {
        this.arg = arg;
        this.message = message;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (arg.equals("error")) {
            sendError();
        }
        if (arg.equals("message")) {
            sendMessage();
        }
    }

    public void sendError() {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("/" + arg + " " + message);
            dos.flush();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-7);
        }
    }

    public void sendMessage() {
        receivers = Server.getConnects();
        senderLogin = receivers.get(socket);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm:ss");
        date = today.format(formatter);

        for (Map.Entry<Socket, String> receiver : receivers.entrySet()) {
            Socket socket = receiver.getKey();
            String log = receiver.getValue();
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("/" + arg + " " + senderLogin + "[" + date + "] : " + message);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-3);
            }
        }
    }
}

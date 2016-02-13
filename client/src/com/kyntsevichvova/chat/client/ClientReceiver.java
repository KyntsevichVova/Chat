package com.kyntsevichvova.chat.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiver implements Runnable {

    private Socket socket;

    public ClientReceiver() {
        socket = Client.getSocket();
        try {
            DataInputStream is = Client.getIS();
            while (true) {
                int length = is.readInt();
                byte[] bytes = new byte[length];
                is.readFully(bytes);
                KLON klon = KLON.parseBytes(bytes);
                if (klon.getString("type").equals("error")) {
                    new ErrorFrame(klon.getString("error"));
                }
                if (klon.getString("type").equals("message")) {
                    ChatFrame.write(klon.getString("author") + "[" + klon.getString("date") + "] : " + klon.getString("message") + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void run() {

    }
}

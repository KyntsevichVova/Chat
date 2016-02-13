package com.kyntsevichvova.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientReceiver implements Runnable {

    private Socket socket;

    public ClientReceiver() {
        socket = Client.getSocket();
        try {
            InputStream is = Client.getIS();
            while (true) {
                int length = is.read() << 24;
                length += is.read() << 16;
                length += is.read() << 8;
                length += is.read();
                byte[] bytes = new byte[length];
                is.read(bytes);
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

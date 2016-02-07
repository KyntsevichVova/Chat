package com.kyntsevichvova.chat.client;

import java.io.DataInputStream;
import java.net.Socket;

public class ClientReceiver implements Runnable {

    private Socket socket;

    @Override
    public void run() {

    }

    public ClientReceiver() {
        socket = Client.getSocket();
        try {
            DataInputStream dis = Client.getDIS();
            while (true) {
                String arg = dis.readUTF();
                String mes = dis.readUTF();
                String tmp = dis.readUTF();
                if (arg.startsWith("/error")) {
                    new ErrorFrame(mes);
                }
                if (arg.startsWith("/message")) {
                    ChatFrame.write(mes);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
    }
}

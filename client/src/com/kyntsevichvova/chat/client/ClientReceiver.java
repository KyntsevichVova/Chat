package com.kyntsevichvova.chat.client;

import java.io.DataInputStream;
import java.io.IOException;
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
                String tmp = dis.readUTF();
                String arg = "", mes = "";
                for (int i = 0; i < tmp.length() && tmp.charAt(i) != ' '; i++) {
                    arg += tmp.charAt(i);
                }
                for (int i = arg.length() + 1; i < tmp.length(); i++) {
                    mes += tmp.charAt(i);
                }
                if (arg.startsWith("/error")) {
                    new ErrorFrame(mes);
                }
                if (arg.startsWith("/message")) {
                    ChatFrame.write(mes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

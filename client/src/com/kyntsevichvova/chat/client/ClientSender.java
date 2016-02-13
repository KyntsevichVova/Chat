package com.kyntsevichvova.chat.client;

import java.io.IOException;
import java.io.OutputStream;

public class ClientSender implements Runnable {

    private String arg;
    private String message;

    public ClientSender(String a, String mes) {
        arg = a;
        message = mes;
    }

    @Override
    public void run() {
        try {
            OutputStream os = Client.getOS();
            KLON klon = Protocol.createChatMessage(arg, message);
            os.write(klon.toFullBytes());
            os.flush();
        } catch (IOException e) {
            new ErrorFrame("Server is disabled");
        }
    }
}

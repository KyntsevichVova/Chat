package com.kyntsevichvova.chat.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class ClientSender implements Runnable {

    private String arg;
    private String message;

    @Override
    public void run() {
        try {
            ObjectOutputStream os = Client.getOS();
            HashMap<String, Object> map = Protocol.createChatMessage(arg, message);
            os.writeObject(map);
            os.flush();
        } catch (IOException e) {
            new ErrorFrame("Server is disabled");
        }
    }

    public ClientSender(String a, String mes) {
        arg = a;
        message = mes;
    }
}

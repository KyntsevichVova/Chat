package com.kyntsevichvova.chat.client;

import java.io.DataOutputStream;
import java.io.IOException;

public class ClientSender implements Runnable {

    private String arg;
    private String message;

    @Override
    public void run() {
        try {
            DataOutputStream dos = Client.getDOS();
            dos.writeUTF("/" + arg + " " + message + "\n");
            dos.flush();
        } catch (IOException e) {
            new ErrorFrame("Server is disabled");
        }
    }

    public ClientSender(String a, String mes) {
        arg = a;
        message = mes;
    }
}

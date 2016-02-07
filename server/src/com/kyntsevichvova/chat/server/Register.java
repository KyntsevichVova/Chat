package com.kyntsevichvova.chat.server;

import java.net.Socket;

public class Register implements Runnable {

    private String arg;
    private Socket socket;

    @Override
    public void run() {
        String log = "";
        String pass = "";
        for (int i = 0; arg.charAt(i) != ' '; i++) {
            log += arg.charAt(i);
        }
        for (int i = log.length() + 1; i != arg.length() && arg.charAt(i) != '\n'; i++) {
            pass += arg.charAt(i);
        }
        if (!Server.register(log, pass)) {
            new Thread(new ServerLogger(String.format("Bad attempt of registering : %nLogin = %s%nPassword = %s", log, pass))).start();
            new Thread(new ServerSender("error", "This login is engaged", socket)).start();
        } else {
            new Thread(new ServerLogger(String.format("New registered : %nLogin = %s%nPassword = %s%n From %s", log, pass, socket))).start();
        }
    }

    public Register(String s, Socket soc) {
        arg = s;
        socket = soc;
    }
}

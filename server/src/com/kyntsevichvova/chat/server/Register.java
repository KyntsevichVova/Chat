package com.kyntsevichvova.chat.server;

import java.net.Socket;

public class Register implements Runnable {

    private String arg;
    private Socket socket;

    public Register(String arg, Socket socket) {
        this.arg = arg;
        this.socket = socket;
    }

    @Override
    public void run() {
        String login = "";
        String password = "";
        for (int i = 0; arg.charAt(i) != ' '; i++) {
            login += arg.charAt(i);
        }
        for (int i = login.length() + 1; i != arg.length() && arg.charAt(i) != '\n'; i++) {
            password += arg.charAt(i);
        }
        if (!Server.register(login, password)) {
            new Thread(new ServerLogger(String.format("Bad attempt of registering : %nLogin = %s%nPassword = %s", login, password))).start();
            new Thread(new ServerSender("error", "This login is engaged", socket)).start();
        } else {
            new Thread(new ServerLogger(String.format("New registered : %nLogin = %s%nPassword = %s%n From %s", login, password, socket))).start();
        }
    }
}

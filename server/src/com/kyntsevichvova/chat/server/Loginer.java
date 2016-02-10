package com.kyntsevichvova.chat.server;

import java.net.Socket;

public class Loginer implements Runnable {

    private String arg;
    private Socket socket;

    public Loginer(Socket socket, String arg) {
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
        if (Server.isRegistered(login, password)) {
            new Thread(new ServerLogger(String.format("New attempt of login :%nLogin = %s%nFrom %s", login, socket))).start();
            Server.logIn(socket, login);
        } else {
            new Thread(new ServerSender("error", "Wrong login/password", socket)).start();
            new Thread(new ServerLogger(String.format("New bad attempt of login :%nLogin = %s%nPassword = %s%nFrom %s", login, password, socket))).start();
        }
    }
}

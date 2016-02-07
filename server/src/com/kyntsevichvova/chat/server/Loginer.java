package com.kyntsevichvova.chat.server;

import java.net.Socket;

public class Loginer implements Runnable {

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
        if (Server.isRegistered(log, pass)) {
            new Thread(new ServerLogger(String.format("New attempt of login :%nLogin = %s%nFrom %s", log, socket))).start();
            Server.logIn(socket, log);
        } else {
            new Thread(new ServerSender("error", "Wrong login/password", socket)).start();
            new Thread(new ServerLogger(String.format("New bad attempt of login :%nLogin = %s%nPassword = %s%nFrom %s", log, pass, socket))).start();
        }
    }

    public Loginer(Socket soc, String s) {
        arg = s;
        socket = soc;
    }
}

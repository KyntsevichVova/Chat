package com.kyntsevichvova.chat.server;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger implements Runnable {

    private PrintWriter os;
    private String date;

    private void getStarted() {
        os = Server.getOS();
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm:ss");
        date = today.format(formatter);
    }

    @Override
    public void run() {

    }

    public ServerLogger() {
        getStarted();
        os.println("----------");
        os.println(date + "/  Server started");
        os.println();
        os.flush();

    }

    public ServerLogger(String s) {
        getStarted();
        os.println(date + "/  " + s);
        os.println();
        os.flush();
    }

}

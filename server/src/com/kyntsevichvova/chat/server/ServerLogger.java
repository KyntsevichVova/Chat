package com.kyntsevichvova.chat.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {

    private static final String LOG_FILE = "log.txt";

    private static PrintWriter pw;

    public static void log(String s) {
        if (pw == null) {
            try {
                pw = new PrintWriter(new FileWriter(Server.SERVER_FOLDER + LOG_FILE, true));
            } catch (IOException e) {
                System.err.println("Can't create log file!");
                return;
            }
        }
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm:ss");
        String date = today.format(formatter);
        System.out.println("[LOG] " + date + "/  " + s);
        System.out.println();
        pw.println(date + "/  " + s);
        pw.println();
        pw.flush();
    }
}

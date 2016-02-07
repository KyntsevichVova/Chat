package com.kyntsevichvova.chat.server;

import java.io.BufferedReader;

public class ServerWorker implements Runnable {

    private BufferedReader br;

    @Override
    public void run() {
        while (true) {
            try {
                String s = br.readLine();
                s = s.toLowerCase();
                if (s.startsWith("/shutdown")) {
                    Server.shutDown();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public ServerWorker() {
        br = Server.getIS();
    }
}

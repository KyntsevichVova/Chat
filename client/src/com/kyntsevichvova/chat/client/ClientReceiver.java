package com.kyntsevichvova.chat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientReceiver implements Runnable {

    private Socket socket;

    @Override
    public void run() {

    }

    public ClientReceiver() {
        socket = Client.getSocket();
        try {
            ObjectInputStream is = Client.getIS();
            while (true) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                try{
                    map = (HashMap<String, Object>) is.readObject();
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                if (map.get("type").equals("error")) {
                    new ErrorFrame((String)map.get("error"));
                }
                if (map.get("type").equals("message")) {
                    ChatFrame.write((String)map.get("author") + "[" + (String)map.get("date") + "] : " + (String)map.get("message") + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

package com.kyntsevichvova.chat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Volodya
 */
public class Client {

    private static int serverPort = 9523;
    private static String serverAddress = "wrt.qjex.xyz";
    private static InetAddress ipAddress;
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static Socket getSocket() {
        return socket;
    }

    public static ObjectOutputStream getOS() {
        return oos;
    }

    public static ObjectInputStream getIS() {
        return ois;
    }

    public static void main(String[] args) {
        try {
            ipAddress = InetAddress.getByName(serverAddress);
            socket = new Socket(ipAddress, serverPort);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            ChatFrame chatFrame = new ChatFrame();
            chatFrame.pack();
            new ClientReceiver();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-4);
        }
    }

}

package com.kyntsevichvova.chat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private static OutputStream os;
    private static InputStream is;

    public static Socket getSocket() {
        return socket;
    }

    public static OutputStream getOS() {
        return os;
    }

    public static InputStream getIS() {
        return is;
    }

    public static void main(String[] args) {
        try {
            ipAddress = InetAddress.getByName(serverAddress);
            socket = new Socket(ipAddress, serverPort);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            ChatFrame chatFrame = new ChatFrame();
            chatFrame.pack();
            new ClientReceiver();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-4);
        }
    }

}

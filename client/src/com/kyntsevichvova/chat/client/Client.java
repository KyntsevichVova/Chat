package com.kyntsevichvova.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Volodya
 */
public class Client {

    private static int serverPort = 9523;
    //private static String serverAddress = "wrt.qjex.xyz";
    private static String serverAddress = "127.0.0.1";
    private static InetAddress ipAddress;
    private static Socket socket;
    private static DataOutputStream dos;
    private static DataInputStream dis;

    public static Socket getSocket() {
        return socket;
    }

    public static DataOutputStream getDOS() {
        return dos;
    }

    public static DataInputStream getDIS() {
        return dis;
    }

    public static void main(String[] args) {
        try {
            ipAddress = InetAddress.getByName(serverAddress);
            socket = new Socket(ipAddress, serverPort);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            ChatFrame chatFrame = new ChatFrame();
            chatFrame.pack();
            new ClientReceiver();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-4);
        }
    }

}

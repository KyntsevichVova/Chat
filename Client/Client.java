package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Володя
 */
public class Client {

    private static int serverPort = 9523;
    private static String serverAddress = "wrt.qjex.xyz";
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

    public static String getLogin() {
        String login = RegisterFrame.getLogin();
        return login;
    }

    public static String getPassword() {
        String password = RegisterFrame.getPassword();
        return password;
    }

    public static void main(String[] args) {
        try {
            ipAddress = InetAddress.getByName(serverAddress);
            socket = new Socket(ipAddress, serverPort);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            ChatFrame chatFrame = new ChatFrame();
            chatFrame.pack();
            ClientReceiver cr = new ClientReceiver();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-4);
        }
    }

}

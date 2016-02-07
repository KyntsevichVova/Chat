package client;

import java.io.DataOutputStream;
import java.net.Socket;

public class ClientSender implements Runnable {

    private String arg;
    private String message;

    @Override
    public void run() {
        try {
            DataOutputStream dos = Client.getDOS();
            dos.writeUTF("/" + arg + "\n");
            dos.writeUTF(message + "\n");
            dos.writeUTF("/endof" + arg + "\n");
            dos.flush();
        } catch (Throwable t) {
            new ErrorFrame("Server is disabled");
        }
    }

    public ClientSender(String a, String mes) {
        arg = a;
        message = mes;
    }
}

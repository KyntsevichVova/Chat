package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerReceiver implements Runnable {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String arg = dis.readUTF();
                String message = dis.readUTF();
                String tmp = dis.readUTF();
                if (Server.isDisconnected(socket) && arg.startsWith("/message")) {
                    new Thread(new ServerSender("error", "You're not logged in", socket)).start();
                    continue;
                }
                if (arg.startsWith("/message")) {
                    new Thread(new ServerLogger(String.format("New message from %s%n%s", socket, message))).start();
                    new Thread(new ServerSender("message", message, socket)).start();
                }
                if (arg.startsWith("/register")) {
                    new Thread(new Register(message, socket)).start();
                }
                if (arg.startsWith("/login")) {
                    new Thread(new Loginer(socket, message)).start();
                }
            } catch (Throwable t) {
                new Thread(new ServerLogger(String.format("Disconnected %s%n", socket))).start();
                Server.disconnect(socket);
                break;
            }
        }
    }

    public ServerReceiver(Socket soc) throws IOException {
        socket = soc;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }
}

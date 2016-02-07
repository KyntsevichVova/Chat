package server;

import java.io.DataOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ServerSender implements Runnable {

    private String arg;
    private String mes;
    private Socket socket;
    private Map<Socket, String> receivers;
    private String date;
    private String senderLogin;

    @Override
    public void run() {
        if (arg.equals("error")) {
            sendError();
        }
        if (arg.equals("message")) {
            sendMessage();
        }
    }

    public void sendError() {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("/" + arg + '\n');
            dos.writeUTF(mes + '\n');
            dos.writeUTF("/endof" + arg + '\n');
            dos.flush();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-7);
        }
    }

    public void sendMessage() {
        receivers = Server.getConnects();
        senderLogin = receivers.get(socket);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm:ss");
        date = today.format(formatter);

        for (Map.Entry<Socket, String> receiver : receivers.entrySet()) {
            Socket soc = receiver.getKey();
            String log = receiver.getValue();
            try {
                DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
                dos.writeUTF("/" + arg + "\n");
                dos.writeUTF(senderLogin + "[" + date + "] : " + mes);
                dos.writeUTF("/endof" + arg);
                dos.flush();
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(-3);
            }
        }
    }

    public ServerSender(String a, String m, Socket soc) {
        arg = a;
        mes = m;
        socket = soc;
    }
}

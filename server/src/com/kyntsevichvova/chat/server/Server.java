package com.kyntsevichvova.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Volodya
 */
public class Server {

    static final String PATH_TO_DB = "e:/share/vova-server/kek/";
    private String DB_NAME = "db.txt";

    private ServerSocket serverSocket;
    private File fileDB;
    private Map<String, String> DB;
    private ServerWorker worker;
    private Connections connections;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.connections = new Connections(this);

        File dir = new File(PATH_TO_DB);
        if (!dir.exists()) {
            System.out.println("Trying to make dir...");
            dir.mkdir();
        }

        fileDB = new File(PATH_TO_DB + DB_NAME);
        if (!fileDB.exists()) {
            System.out.println("Trying to make DB file...");
            fileDB.createNewFile();
        }

        DB = new HashMap<>();

        try {
            if (fileDB.exists()) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileDB))) {
                    DB = (HashMap<String, String>) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("DB file is empty or corrupted");
            System.out.println("Creating new DB...");
        }

        this.worker = new ServerWorker(this);
    }

    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(9523);
        } catch (IOException e) {
            //Unable to start
            return;
        }
        server.start();
        ServerLogger.log("Init complete");
    }

    public void shutDown() {
        ServerLogger.log("Server is shut down");
        System.exit(0);
    }

    public boolean register123(String login, String password) {
        if (DB.containsKey(login)) {
            return false;
        }
        DB.put(login, password);
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileDB))) {
            os.writeObject(DB);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isRegistered(String login, String password) {
        return (DB.containsKey(login) && DB.get(login).equals(password));
    }

    private void login() {
        String login = "";
        String password = "";
        for (int i = 0; arg.charAt(i) != ' '; i++) {
            login += arg.charAt(i);
        }
        for (int i = login.length() + 1; i != arg.length() && arg.charAt(i) != '\n'; i++) {
            password += arg.charAt(i);
        }
        if (Server.isRegistered(login, password)) {
            new Thread(new ServerLogger(String.format("New attempt of login :%nLogin = %s%nFrom %s", login, socket))).start();
            Server.logIn(socket, login);
        } else {
            new Thread(new ServerSender("error", "Wrong login/password", socket)).start();
            new Thread(new ServerLogger(String.format("New bad attempt of login :%nLogin = %s%nPassword = %s%nFrom %s", login, password, socket))).start();
        }
    }

    private void register(String message, ClientConnection connection) {
        String login = "";
        String password = "";
        for (int i = 0; message.charAt(i) != ' '; i++) {
            login += message.charAt(i);
        }
        for (int i = login.length() + 1; i != message.length() && message.charAt(i) != '\n'; i++) {
            password += message.charAt(i);
        }
        if (!Server.register(login, password)) {
            new Thread(new ServerLogger(String.format("Bad attempt of registering : %nLogin = %s%nPassword = %s", login, password))).start();
            new Thread(new ServerSender("error", "This login is engaged", socket)).start();
        } else {
            new Thread(new ServerLogger(String.format("New registered : %nLogin = %s%nPassword = %s%n From %s", login, password, socket))).start();
        }
    }

    public void onMessage(String tmp, ClientConnection connection) {
        String arg = "", message = "";
        for (int i = 0; i < tmp.length() && tmp.charAt(i) != ' '; i++) {
            arg += tmp.charAt(i);
        }
        for (int i = arg.length() + 1; i < tmp.length(); i++) {
            message += tmp.charAt(i);
        }
        if (connections.isDisconnected(connection) && arg.startsWith("/message")) {
            connection.sendError("You're not logged in");
            return;
        }
        if (arg.startsWith("/message")) {
            ServerLogger.log(String.format("New message from %s%n%s", connection, message));
            connections.broadcastMessage(message, connection);
        }
        if (arg.startsWith("/register")) {
            register(message, connection);
        }
        if (arg.startsWith("/login")) {
            login(socket, message);
        }
    }

    public void disconnect(ClientConnection connection) {
        connections.disconnect(connection);
    }

    public void start() {
        this.worker.start();
        this.startSocketHandler();
    }

    private void startSocketHandler() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ServerLogger.log(String.format("New socket connected : %s", socket));
                    new Thread(new ClientConnection(socket, this)).start();
                } catch (IOException e) {
                    //
                }
            }
        }).start();
    }
}

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

    //static final String PATH_TO_DB = "e:/share/vova-server/kek/";
    static final String PATH_TO_DB = "c:/home/vova/ ";

    private String DB_NAME = "db.txt";

    private ServerSocket serverSocket;
    private File fileDB;
    private Map<String, String> DB;
    private Connections connections;

    private ServerWorker worker;
    private Thread socketHandler;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.connections = new Connections(this);

        File dir = new File(PATH_TO_DB);
        if (!dir.exists()) {
            ServerLogger.log("Trying to make dir...");
            dir.mkdir();
        }

        fileDB = new File(PATH_TO_DB + DB_NAME);
        if (!fileDB.exists()) {
            ServerLogger.log("Trying to make DB file...");
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
            ServerLogger.log("DB file is empty or corrupted");
            ServerLogger.log("Creating new DB...");
        }

        this.worker = new ServerWorker(this);

        this.socketHandler = new Thread(() -> {
            while (!serverSocket.isClosed() && !Thread.interrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    ServerLogger.log(String.format("New socket connected : %s", socket));
                    new ClientConnection(socket, this).start();

                } catch (IOException e) {
                    //
                }
            }
        });
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
        server.join();
        ServerLogger.log("End of main was reached");
    }

    public void shutDown() {
        ServerLogger.log("Server stop signal received");
        this.stop();
    }

    private void updateDB() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileDB))) {
            os.writeObject(DB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkAuth(String login, String password) {
        return (DB.containsKey(login) && DB.get(login).equals(password));
    }

    private boolean checkRegister(String login) {
        return !DB.containsKey(login);
    }

    private void login(String message, ClientConnection connection) {
        String login = "";
        String password = "";
        for (int i = 0; message.charAt(i) != ' '; i++) {
            login += message.charAt(i);
        }
        for (int i = login.length() + 1; i != message.length() && message.charAt(i) != '\n'; i++) {
            password += message.charAt(i);
        }
        if (checkAuth(login, password)) {
            ServerLogger.log(String.format("New attempt of login :%nLogin = %s%nFrom %s", login, connection));
            connection.setLogin(login);
            connections.connect(connection, login);
        } else {
            connection.sendError("Wrong login/password");
            ServerLogger.log(String.format("New bad attempt of login :%nLogin = %s%nPassword = %s%nFrom %s", login, password, connection));
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
        if (checkRegister(login)) {
            DB.put(login, password);
            updateDB();
            ServerLogger.log(String.format("New registered : %nLogin = %s%nPassword = %s%n From %s", login, password, connection));
        } else {
            ServerLogger.log(String.format("Bad attempt of registering : %nLogin = %s%nPassword = %s", login, password));
            connection.sendError("This login is engaged");
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
            login(message, connection);
        }
    }

    public void disconnect(ClientConnection connection) {
        connections.disconnect(connection);
    }

    public void start() {
        this.worker.start();
        this.socketHandler.start();
        ServerLogger.log("Server started");
    }

    public void join() {
        this.worker.join();
        ServerLogger.log("Joined in ServerWorker");
        try {
            this.socketHandler.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServerLogger.log("Joined in SocketHandler");
    }

    public void stop() {
        this.worker.stop();
        ServerLogger.log("ServerWorker is stopped");
        this.socketHandler.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerLogger.log("ServerHandler is interrupted");
        ClientConnection.stopAll(this);
    }
}

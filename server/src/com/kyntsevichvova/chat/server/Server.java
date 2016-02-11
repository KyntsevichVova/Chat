package com.kyntsevichvova.chat.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 *
 * @author Volodya
 */
public class Server {

    static final String SERVER_FOLDER = "c:/home/vova/";

    private ServerSocket serverSocket;
    private Connections connections;
    private Auth auth;

    private ServerWorker worker;
    private Thread socketHandler;

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.connections = new Connections(this);

        File dir = new File(SERVER_FOLDER);
        if (!dir.exists()) {
            ServerLogger.log("Trying to make dir...");
            dir.mkdir();
        }

        this.auth = new Auth(Paths.get(SERVER_FOLDER, "db.txt"));

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

    public String getDate() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm:ss");
        return today.format(formatter);
    }

    public void shutDown() {
        ServerLogger.log("Server stop signal received");
        this.stop();
    }

    private void signIn(String login, String password, ClientConnection connection) {
        signOut(connection);
        if (auth.signIn(login, password)) {
            connection.setLogin(login);
            connections.connect(connection, login);
            connections.broadcast(Protocol.createChatMessage("info", this.getDate(), login + " вошел в чат"));
            ServerLogger.log(String.format("New attempt of signing in :%nLogin = %s%nFrom %s", login, connection));
        } else {
            connection.sendError("Wrong login/password");
            ServerLogger.log(String.format("New bad attempt of signing in :%nLogin = %s%nPassword = %s%nFrom %s", login, password, connection));
        }
    }

    private void signUp(String login, String password, ClientConnection connection) {
        signOut(connection);
        if (auth.signUp(login, password)) {
            ServerLogger.log(String.format("New registered : %nLogin = %s%nPassword = %s%n From %s", login, password, connection));
            connections.broadcast(Protocol.createChatMessage("info", this.getDate(), "Он зарегистрировался! => " + login));
        } else {
            connection.sendError("This login is engaged");
            ServerLogger.log(String.format("Bad attempt of registering : %nLogin = %s%nPassword = %s", login, password));
        }
    }

    public void signOut(ClientConnection connection) {
        if (connection.getLogin() != null) {
            connections.broadcast(Protocol.createChatMessage("info", this.getDate(), connection.getLogin() + " вышел из чата"));
        }
        connections.disconnect(connection);
        connection.setLogin(null);
    }

    public void onMessage(HashMap<String, Object> map, ClientConnection connection) {
        try {
            if (map.get("type").equals("message")) {
                if (connections.isDisconnected(connection)) {
                    connection.sendError("You're not logged in");
                    return;
                }
                String message = (String) map.get("message");
                message = message.replace("\n", "");
                ServerLogger.log(String.format("New message from %s%n%s", connection, message));
                connections.broadcast(Protocol.createChatMessage(connection.getLogin(), this.getDate(), message));
                return;
            }

            if (map.get("type").equals("signout")) {
                signOut(connection);
                return;
            }

            if (map.get("type").equals("signin")) {
                signIn((String) map.get("login"), (String) map.get("password"), connection);
                return;
            }

            if (map.get("type").equals("signup")) {
                signUp((String) map.get("login"), (String) map.get("password"), connection);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect(ClientConnection connection) {
        this.signOut(connection);
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

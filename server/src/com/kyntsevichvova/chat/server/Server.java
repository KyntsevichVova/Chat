package com.kyntsevichvova.chat.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;

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

    public void shutDown() {
        ServerLogger.log("Server stop signal received");
        this.stop();
    }

    private void signIn(String login, String password, ClientConnection connection) {
        if (auth.signIn(login, password)) {
            connection.setLogin(login);
            connections.connect(connection, login);
            ServerLogger.log(String.format("New attempt of signing in :%nLogin = %s%nFrom %s", login, connection));
        } else {
            connection.sendError("Wrong login/password");
            ServerLogger.log(String.format("New bad attempt of signing in :%nLogin = %s%nPassword = %s%nFrom %s", login, password, connection));
        }
    }

    private void signUp(String login, String password, ClientConnection connection) {
        if (auth.signUp(login, password)) {
            ServerLogger.log(String.format("New registered : %nLogin = %s%nPassword = %s%n From %s", login, password, connection));
        } else {
            connection.sendError("This login is engaged");
            ServerLogger.log(String.format("Bad attempt of registering : %nLogin = %s%nPassword = %s", login, password));
        }
    }

    private void parse(String arg, String mes, ClientConnection connection) {
        String login = "";
        String password = "";
        for (int i = 0; mes.charAt(i) != ' '; i++) {
            login += mes.charAt(i);
        }
        for (int i = login.length() + 1; i != mes.length() && mes.charAt(i) != '\n'; i++) {
            password += mes.charAt(i);
        }
        if (arg.startsWith("/signup"))
            signUp(login, password, connection);
        else
            signIn(login, password, connection);

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
        if (arg.startsWith("/sign")) {
            parse(arg, message, connection);
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

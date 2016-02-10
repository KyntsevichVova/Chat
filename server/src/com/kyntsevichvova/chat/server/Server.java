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

    static private ServerSocket serverSocket;
    private static File fileDB;
    private static File fileLog;
    private static Map<String, String> DB;
    private static Map<Socket, String> connected;
    private static BufferedReader br;
    private static PrintWriter pw;
    private int port = 9523;
    //private String pathToDB = "C:\\DB\\";
    private String pathToDB = "e:/share/vova-server/kek/";
    private String nameDB = "db.txt";
    private String nameLog = "log.txt";

    public Server() throws Throwable {
        serverSocket = new ServerSocket(port);
        try {
            File dir = new File(pathToDB);
            if (!dir.exists()) {
                System.out.println("Trying to make dir...");
                dir.mkdir();
            }
        } catch (Throwable t) {
            System.out.println("Dir is not founded");
        }
        try {
            fileDB = new File(pathToDB + nameDB);
            if (!fileDB.exists()) {
                System.out.println("Trying to make DB file...");
                fileDB.createNewFile();
            }
        } catch (Throwable t) {
            System.out.println("File of DB is not founded");
        }
        try {
            fileLog = new File(pathToDB + nameLog);
            if (!fileLog.exists()) {
                System.out.println("Trying to make log file...");
                fileLog.createNewFile();
            }
        } catch (Throwable t) {
            System.out.println("File of log is not founded");
        }
        try {
            System.out.println("Trying to get InputStream...");
            br = new BufferedReader(new InputStreamReader(System.in));
        } catch (Throwable t) {
            System.out.println("Can't get InputStream");
        }
        try {
            System.out.println("Trying to get OutputStream...");
            pw = new PrintWriter(new BufferedWriter(new FileWriter(fileLog, true)));
        } catch (Throwable t) {
            System.out.println("Can't get OutputStream");
        }
        DB = new HashMap<>();
        connected = new HashMap<>();

        try {
            if (fileDB.exists()) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileDB))) {
                    DB = (HashMap<String, String>) in.readObject();
                }
            }
        } catch (Throwable t) {

            System.out.println("DB file is empty or corrupted");
            System.out.println("Creating new DB...");
            DB = new HashMap<>();
        }
    }

    public static Map<Socket, String> getConnects() {
        return connected;
    }

    public static BufferedReader getIS() {
        return br;
    }

    public static PrintWriter getOS() {
        return pw;
    }

    public static void shutDown() {
        new Thread(new ServerLogger("Server is shut down")).start();
        System.exit(0);
    }

    public static boolean isDisconnected(Socket socket) {
        return !connected.containsKey(socket);
    }

    public static void disconnect(Socket soc) {
        connected.remove(soc);
    }

    public static boolean register(String name, String pass) {
        if (DB.containsKey(name)) {
            return false;
        }
        DB.put(name, pass);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileDB));
            os.writeObject(DB);
            os.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return true;
    }

    public static boolean isRegistered(String login, String password) {
        return (DB.containsKey(login) && DB.get(login).equals(password));
    }

    public static void logIn(Socket soc, String name) {
        connected.put(soc, name);
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            new Thread(new ServerWorker()).start();
            new Thread(new ServerLogger()).start();
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerLogger(String.format("New socket connected : %s", socket))).start();
                new Thread(new ServerReceiver(socket)).start();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        } finally {
            new Thread(new ServerLogger("Server is shut down")).start();
        }
    }

}

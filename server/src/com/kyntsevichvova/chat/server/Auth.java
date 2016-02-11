package com.kyntsevichvova.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by petuh on 2/11/2016.
 */
public class Auth {

    private Map<String, String> db;
    private Path dbPath;

    public Auth(Path dbPath) {
        this.dbPath = dbPath;
        db = new HashMap<>();

        try {
            if (Files.exists(dbPath)) {
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(dbPath))) {
                    this.db = (HashMap<String, String>) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            ServerLogger.log("DB file is empty or corrupted");
            ServerLogger.log("Creating new DB...");
        }
    }

    private void updateDB() {
        try (ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(dbPath))) {
            os.writeObject(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean signIn(String login, String password) {
        return (db.containsKey(login) && db.get(login).equals(password));
    }

    public boolean signUp(String login, String password) {
        if (db.containsKey(login)) return false;
        db.put(login, password);
        updateDB();
        return true;
    }
}

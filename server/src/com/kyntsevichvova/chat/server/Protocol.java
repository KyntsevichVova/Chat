package com.kyntsevichvova.chat.server;

import java.util.HashMap;

/**
 * Created by petuh on 2/11/2016.
 */
public class Protocol {
    public static HashMap<String, Object> createChatMessage(String author, String date, String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "message");
        map.put("author", author);
        map.put("date", date);
        map.put("message", message);
        return map;
    }

    public static HashMap<String, Object> createErrorMessage(String error) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "error");
        map.put("error", error);
        return map;
    }
}

package com.kyntsevichvova.chat.server;

/**
 * Created by petuh on 2/11/2016.
 */
public class Protocol {
    public static KLON createChatMessage(String author, String date, String message) {
        return new KLON().putString("type", "message")
                .putString("author", author)
                .putString("date", date)
                .putString("message", message);
    }

    public static KLON createErrorMessage(String error) {
        return new KLON().putString("type", "error")
                .putString("error", error);
    }
}

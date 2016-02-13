package com.kyntsevichvova.chat.client;

public class Protocol {
    public static KLON createChatMessage(String arg, String mes) {
        KLON klon = new KLON().putString("type", arg);
        if (arg.equals("message")){
            klon.putString("message", mes);
        }
        else{
            int pos = mes.indexOf(' ');
            String login = mes.substring(0, pos);
            String password = mes.substring(pos + 1);
            klon.putString("login", login)
                    .putString("password", password);
        }
        return klon;
    }
}

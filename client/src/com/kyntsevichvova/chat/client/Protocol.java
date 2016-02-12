package com.kyntsevichvova.chat.client;

import java.util.HashMap;

public class Protocol {
    public static HashMap<String, Object> createChatMessage(String arg, String mes){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", arg);
        if (arg.equals("message")){
            map.put("message", mes);
        }
        else{
            int pos = mes.indexOf(' ');
            String login = mes.substring(0, pos);
            String password = mes.substring(pos + 1);
            map.put("login", login);
            map.put("password", password);
        }
        return map;
    }
}

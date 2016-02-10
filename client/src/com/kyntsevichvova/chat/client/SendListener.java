package com.kyntsevichvova.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String mes = ChatFrame.getMessage();
        mes = mes.trim();
        if (mes.length() > 0) {
            new Thread(new ClientSender("message", mes)).start();
        }
    }
}

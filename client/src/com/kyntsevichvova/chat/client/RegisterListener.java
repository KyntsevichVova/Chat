package com.kyntsevichvova.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String login = Client.getLogin();
        String password = Client.getPassword();
        if (check(login) && check(password)) {
            RegisterFrame.getInstance().dispose();
            new Thread(new ClientSender("register", login + " " + password)).start();
        } else {
            new ErrorFrame("Forbidden login/password");
        }
    }

    public boolean check(String s) {
        if (s.length() == 0) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                return false;
            }
        }
        return true;
    }

}

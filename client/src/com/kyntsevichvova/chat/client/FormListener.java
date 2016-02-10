package com.kyntsevichvova.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormListener implements ActionListener {

    private FormFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        String login = frame.getLogin();
        String password = frame.getPassword();

        if (check(login) && check(password)) {
            frame.getInstance().dispose();
            new Thread(new ClientSender("sign" + frame.getTypeOf(), login + " " + password)).start();
        } else {
            new ErrorFrame("Forbidden login/password");
        }
    }

    public FormListener(FormFrame f) {
        frame = f;
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

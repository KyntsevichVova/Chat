package com.kyntsevichvova.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        new FormFrame(s);
    }
}

package com.kyntsevichvova.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class ExitListener implements ActionListener {

    private JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        frame.dispose();
    }

    public ExitListener(JFrame f) {
        frame = f;
    }
}

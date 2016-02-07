package com.kyntsevichvova.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class runLogListener  implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
        new LoginFrame();
    }
}

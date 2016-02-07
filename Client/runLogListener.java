package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class runLogListener  implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
        new LoginFrame();
    }
}

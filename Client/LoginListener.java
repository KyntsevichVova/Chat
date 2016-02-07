package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;

public class LoginListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String login = LoginFrame.getLogin();
        String password = LoginFrame.getPassword();

        if (check(login) && check(password)) {
            LoginFrame.getInstance().dispose();
            new Thread(new ClientSender("login", login + " " + password)).start();
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

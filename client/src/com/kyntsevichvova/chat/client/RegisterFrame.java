package com.kyntsevichvova.chat.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterFrame extends JFrame {

    public static JTextField logField;
    public static JTextField passField;
    private static RegisterFrame instance;

    public RegisterFrame() {
        super("Registration");
        instance = this;
        createGUI();
    }

    public static RegisterFrame getInstance() {
        return instance;
    }

    public static String getLogin() {
        String login = logField.getText();
        logField.setText("");
        return login;
    }

    public static String getPassword() {
        String password = passField.getText();
        passField.setText("");
        return password;
    }

    public void createGUI() {
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label1 = new JLabel("Login :");
        JLabel label2 = new JLabel("Password : ");

        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        JButton button = new JButton("Register");

        logField = new JTextField();
        logField.setColumns(20);
        passField = new JTextField();
        passField.setColumns(20);

        RegisterListener rlist = new RegisterListener();
        button.addActionListener(rlist);
        logField.addActionListener(rlist);
        passField.addActionListener(rlist);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel1.setLayout(new FlowLayout());
        panel2.setLayout(new FlowLayout());
        panel3.setLayout(new FlowLayout());

        panel1.add(label1);
        panel1.add(logField);

        panel2.add(label2);
        panel2.add(passField);

        panel3.add(button);

        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);

        getContentPane().add(panel);

        setPreferredSize(new Dimension(450, 130));
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

    }

}

package com.kyntsevichvova.chat.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormFrame extends JFrame {

    private JTextField logField;
    private JTextField passField;
    private FormFrame instance;
    private String type;

    public FormFrame(String s) {
        super("Signing " + s);
        type = s;
        instance = this;
        createGUI();
    }

    public String getTypeOf() {
        return type;
    }

    public FormFrame getInstance() {
        return instance;
    }

    public String getLogin() {
        String login = logField.getText();
        logField.setText("");
        return login;
    }

    public String getPassword() {
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

        JButton button = new JButton("Sign " + type);

        logField = new JTextField();
        logField.setColumns(20);
        passField = new JTextField();
        passField.setColumns(20);

        FormListener list = new FormListener(instance);
        button.addActionListener(list);
        logField.addActionListener(list);
        passField.addActionListener(list);

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

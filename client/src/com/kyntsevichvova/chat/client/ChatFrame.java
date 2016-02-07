package com.kyntsevichvova.chat.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatFrame extends JFrame {

    private static JTextArea textArea;
    private static JTextField textField;
    private JButton regButton;
    private JButton logButton;
    private JButton sendButton;
    public static ChatFrame instance;

    public ChatFrame() {
        super("Chat");
        instance = this;
        createGUI();
    }

    public static void write(String mes) {
        System.out.println(mes);
        textArea.append(mes);
    }

    public static String getMessage() {
        String text = textField.getText();
        textField.setText("");
        return text;
    }

    public void createGUI() {
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        regButton = new JButton("Register");
        logButton = new JButton("Log In");
        sendButton = new JButton("Send");

        runRegListener reglist = new runRegListener();
        regButton.addActionListener(reglist);
        runLogListener loglist = new runLogListener();
        logButton.addActionListener(loglist);
        SendListener sendlist = new SendListener();
        sendButton.addActionListener(sendlist);

        JPanel upPanel = new JPanel();
        upPanel.setLayout(new FlowLayout());
        upPanel.add(regButton);
        upPanel.add(logButton);

        textArea = new JTextArea(100, 100);
        JScrollPane chat = new JScrollPane(textArea);
        textArea.setEditable(false);
        textField = new JTextField();
        textField.setColumns(30);
        textField.addActionListener(sendlist);

        JLabel label = new JLabel("Write Message");
        JPanel dPanel = new JPanel();
        dPanel.setLayout(new FlowLayout());
        dPanel.add(label);
        dPanel.add(textField);
        dPanel.add(sendButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(upPanel);
        panel.add(chat);
        panel.add(dPanel);

        getContentPane().add(panel);
        setPreferredSize(new Dimension(800, 600));
        setVisible(true);
        //setLocationRelativeTo(null);
    }
}

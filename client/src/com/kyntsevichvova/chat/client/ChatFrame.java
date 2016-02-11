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
    private JButton clearButton;
    private static JButton swipeButton;
    private static boolean swipe = true;
    public static ChatFrame instance;

    public ChatFrame() {
        super("Chat");
        instance = this;
        createGUI();
    }

    public static void write(String mes) {
        System.out.println(mes);
        textArea.append(mes);
        if (swipe)
            textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public static String getMessage() {
        String text = textField.getText();
        textField.setText("");
        return text;
    }

    public static void clearTextArea() {
        textArea.setText(null);
    }
    
    public static void changeSwipe(){
        swipe = !swipe;
        if (swipe)
            swipeButton.setText("Swiping is ON");
        else
            swipeButton.setText("Swiping is OFF");
        if (swipe)
            textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void createGUI() {
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        regButton = new JButton("Register");
        logButton = new JButton("Log In");
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear chat");
        swipeButton = new JButton("Swiping ON");
        
        RunListener list = new RunListener();
        regButton.setActionCommand("up");
        regButton.addActionListener(list);
        logButton.setActionCommand("in");
        logButton.addActionListener(list);
        SendListener slist = new SendListener();
        sendButton.addActionListener(slist);
        ClearListener clist = new ClearListener();
        clearButton.addActionListener(clist);
        SwipeListener swlist = new SwipeListener();
        swipeButton.addActionListener(swlist);

        JPanel upPanel = new JPanel();
        upPanel.setLayout(new FlowLayout());
        upPanel.add(regButton);
        upPanel.add(logButton);
        upPanel.add(clearButton);
        upPanel.add(swipeButton);

        textArea = new JTextArea(100, 100);
        JScrollPane chat = new JScrollPane(textArea);
        textArea.setEditable(false);
        textField = new JTextField();
        textField.setColumns(50);
        textField.addActionListener(slist);

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
    }
}

package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ErrorFrame extends JFrame {

    public static ErrorFrame instance;
    private String message;

    public ErrorFrame(String s) {
        super("Error!!");
        message = s;
        instance = this;
        createGUI();
    }

    public static ErrorFrame getInstance() {
        return instance;
    }

    public void createGUI() {
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        JButton button = new JButton("OK");
        ExitListener list = new ExitListener(instance);
        button.addActionListener(list);
        panel.add(label, BorderLayout.PAGE_START);
        panel.add(button, BorderLayout.PAGE_END);
        getContentPane().add(panel);
        setPreferredSize(new Dimension(200, 100));
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

}

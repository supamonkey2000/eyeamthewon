package com.eyeamthewon.editor;


import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Client extends JFrame {
    private JTextField filenameTF, addressTF;
    private JTextArea contentTF;

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Client() {
        super("EyeAmTheWon Editor");
        setLayout(new FlowLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        filenameTF = new JTextField("/var/www/html/filename.extension");
        contentTF = new JTextArea(24,50);
        //contentTF.setSize(590,430);
        JButton requestPageB = new JButton("Request Page");
        JButton updatePageB = new JButton("Update Page");
        JButton newPageB = new JButton("New Page");
        addressTF = new JTextField("255.255.255.255");
        JButton connectB = new JButton("Connect");

        setLocationRelativeTo(null);
        setResizable(false);

        gbc.gridx = 0; gbc.gridy = 0;
        add(addressTF,gbc);
        gbc.gridx = 1; add(connectB,gbc);
        gbc.gridx = 2;gbc.gridy = 0;
        add(filenameTF, gbc);
        gbc.gridy = 1;add(contentTF, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        add(requestPageB,gbc);
        gbc.gridx = 1; add(updatePageB,gbc);
        gbc.gridx = 2; add(newPageB,gbc);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640,480);
        setVisible(true);

        connectB.addActionListener(e -> {
            try {
                socket = new Socket(addressTF.getText(), Server.PORT);
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
                addressTF.setText("Connected");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        requestPageB.addActionListener(e -> {
            try {
                output.writeObject(Integer.toString(NetCode.REQUEST_PAGE) + filenameTF.getText());
                output.flush();
                System.out.println("Requested Page!");
                String content = input.readObject().toString();
                System.out.println("Page Received!");
                contentTF.setText(content);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        updatePageB.addActionListener(e -> {
            try {
                String data = Integer.toString(NetCode.UPDATE_PAGE) + Pattern.quote("[[[FILE[[") + filenameTF.getText() + "]]FILE]]]" + contentTF.getText();
                output.writeObject(data);
                output.flush();
                System.out.println("Sent updated page!");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });

        newPageB.addActionListener(e -> {
            try {
                output.writeObject(Integer.toString(NetCode.CREATE_PAGE) + filenameTF.getText());
                output.flush();
                System.out.println("Sent page creation request!");
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}

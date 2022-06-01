package org.suai.view;

import org.suai.controller.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PanelCreate extends JPanel implements ActionListener {

    private GameClient window;

    private final JLabel text = new JLabel("Такое имя уже существует");
    private final JTextField inputName = new JTextField();
    private final JPasswordField inputPassword = new JPasswordField();
    private final JButton visibility = new JButton("видимость");
    private final JButton send = new JButton("Отправить");
    private int click = 0;

    public PanelCreate(GameClient window) {
        this.window = window;

        setBackground(new Color(49, 38, 79));
        setLayout(null);

        text.setFont(new Font("Serif", Font.PLAIN, 25));
        text.setForeground(Color.red);
        text.setBounds(300, 240,500, 50);
        text.setVisible(false);

        JLabel text1 = new JLabel("Название сервера");
        text1.setFont(new Font("Serif", Font.PLAIN, 30));
        text1.setBounds(300, 200,400, 50);
        text1.setForeground(new Color(83, 69, 122));

        inputName.setFont(new Font("Serif", Font.PLAIN, 30));
        inputName.setBounds(300, 300,400, 50);
        inputName.setForeground(new Color(83, 69, 122));
        inputName.setBackground(new Color(255, 242, 122));

        JLabel text2 = new JLabel("Пароль");
        text2.setFont(new Font("Serif", Font.PLAIN, 30));
        text2.setBounds(300, 400,400, 50);
        text2.setForeground(new Color(83, 69, 122));

        inputPassword.setFont(new Font("Serif", Font.PLAIN, 30));
        inputPassword.setBounds(300, 500,400, 50);
        inputPassword.setForeground(new Color(83, 69, 122));
        inputPassword.setBackground(new Color(255, 242, 122));

        visibility.setFont(new Font("Serif", Font.PLAIN, 20));
        visibility.setBounds(510, 500,150, 50);
        visibility.setForeground(new Color(83, 69, 122));
        visibility.addActionListener(this);

        send.setFont(new Font("Serif", Font.PLAIN, 20));
        send.setBounds(300, 600,150, 50);
        send.setForeground(new Color(83, 69, 122));
        send.addActionListener(this);

        add(text1);
        add(inputName);
        add(text2);
        add(inputPassword);
        add(visibility);
        add(send);
        add(text);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == visibility) {

            if (click % 2 == 0) {
                inputPassword.setEchoChar((char) 0);
            } else {
                inputPassword.setEchoChar('•');
            }
            click++;

        } else if (e.getSource() == send) {
            try {
                if(window.sendGame("newGame " + inputName.getText() + " " + String.valueOf(inputPassword.getPassword()) + "\n")) {
                    window.createArena(0);
                    setVisible(false);
                } else {
                    inputName.setText(null);
                    inputPassword.setText(null);
                    text.setVisible(true);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

package org.suai.view;

import org.suai.controller.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PanelEnterPassword extends JPanel implements ActionListener {

    private GameClient window;
    private String nameOfGame;
    private final JLabel text = new JLabel("Неверный пароль");
    private final JPasswordField inputPassword = new JPasswordField();
    private final JButton visibility = new JButton("видимость");
    private final JButton send = new JButton("Отправить");
    private int click = 0;

    public PanelEnterPassword(GameClient window, String nameOfGame){
        this.window = window;
        this.nameOfGame = nameOfGame;
        setBackground(new Color(49, 38, 79));
        setLayout(null);

        text.setFont(new Font("Serif", Font.PLAIN, 25));
        text.setForeground(Color.red);
        text.setBounds(300, 200,500, 50);
        text.setVisible(false);

        inputPassword.setFont(new Font("Serif", Font.PLAIN, 30));
        inputPassword.setBounds(300, 300,400, 50);
        inputPassword.setForeground(new Color(83, 69, 122));
        inputPassword.setBackground(new Color(255, 242, 122));

        visibility.setFont(new Font("Serif", Font.PLAIN, 20));
        visibility.setBounds(510, 300,150, 50);
        visibility.setForeground(new Color(83, 69, 122));
        visibility.addActionListener(this);

        send.setFont(new Font("Serif", Font.PLAIN, 20));
        send.setBounds(300, 400,150, 50);
        send.setForeground(new Color(83, 69, 122));
        send.addActionListener(this);

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
                int res = window.sendPassword("checkGamePassword " + nameOfGame + " "
                        + String.valueOf(inputPassword.getPassword()) + "\n");
                if(res != -1) {
                    window.createArena(res);
                    setVisible(false);
                } else {
                    inputPassword.setText(null);
                    text.setVisible(true);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

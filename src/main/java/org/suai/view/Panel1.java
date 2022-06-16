package org.suai.view;

import org.suai.controller.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Panel1 extends JPanel implements ActionListener {

    private JButton connectionButton = new JButton("Выбрать сервер");
    private JButton createButton = new JButton("Создать сервер");
    private GameClient window;

    public Panel1(GameClient window) {
        this.window = window;
        setBackground(new Color(49, 38, 79));
        setLayout(null);

        createButton.addActionListener(this);
        createButton.setFont(new Font("Serif", Font.PLAIN, 20));
        createButton.setForeground(new Color(83, 69, 122));
        createButton.setBounds(300, 200, 300, 100);

        connectionButton.addActionListener(this);
        connectionButton.setFont(new Font("Serif", Font.PLAIN, 20));
        connectionButton.setForeground(new Color(83, 69, 122));
        connectionButton.setBounds(300, 400, 300, 100);

        //add(BorderLayout.CENTER, createButton);
        //add(BorderLayout.CENTER, connectionButton);
        add(createButton);
        add(connectionButton);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == connectionButton) {

            setVisible(false);
            PanelConnection panel = null;
            try {
                panel = new PanelConnection(window);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            window.add(panel);

        } else if (e.getSource() == createButton) {

            setVisible(false);
            PanelCreate panel = new PanelCreate(window);
            window.add(panel);
        }
    }
}

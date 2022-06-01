package org.suai.view;

import org.suai.controller.client.GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PanelConnection extends JPanel implements ActionListener {

    private GameClient window;

    private JList<String> list;
    private final JButton okButton = new JButton("Выбрать");

    public PanelConnection(GameClient window) throws IOException {
        this.window = window;
        setLayout(null);
        String[] games = window.getGames();
        //games[0] = "Нет активных серверов";
        if (games[0].equals("")) {
            games[0] = "Нет активных серверов";
            okButton.setVisible(false);
        }
        //System.out.println();

        setBackground(new Color(49, 38, 79));

        JLabel text1 = new JLabel("Активные сервера");
        text1.setFont(new Font("Serif", Font.PLAIN, 80));
        text1.setBounds(100, 10,700, 100);
        text1.setForeground(new Color(83, 69, 122));

        list = new JList<>(games);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setFont(new Font("Serif", Font.PLAIN, 60));
        list.setForeground(new Color(255, 242, 122));

        list.setBackground(new Color(83, 69, 122));
        list.setVisible(true);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBounds(100, 110, 700, 400);
        scrollPane.createVerticalScrollBar();
        scrollPane.setVisible(true);
        scrollPane.setVisible(true);

        okButton.setFont(new Font("Serif", Font.PLAIN, 20));
        okButton.setBounds(300, 550,150, 50);
        okButton.setForeground(new Color(83, 69, 122));
        okButton.addActionListener(this);


        add(scrollPane);
        add(text1);
        add(okButton);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == okButton) {
            setVisible(false);
            String string = list.getSelectedValue();
            PanelEnterPassword panel = new PanelEnterPassword(window, string);
            window.add(panel);
        }

    }
}

package org.suai.trySwing.human;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HumanData extends JFrame implements ActionListener {

    private PanelData panelData;
    private JButton okButton = new JButton("OK");

    public HumanData() {
        setSize(400, 500);

        panelData = new PanelData();
        add(BorderLayout.CENTER, panelData);
        add(BorderLayout.SOUTH, okButton);
        okButton.addActionListener(this);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            JOptionPane.showMessageDialog(null,
                    panelData.getData(),
                    "Information about a person",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public static void main(String[] args) {
        new HumanData();
    }
}

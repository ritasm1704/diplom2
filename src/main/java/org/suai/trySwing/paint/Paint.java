package org.suai.trySwing.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Paint extends JFrame implements ActionListener {
    JButton green = new JButton();
    JButton red = new JButton();
    JButton yellow = new JButton();
    JButton blue = new JButton();
    JButton magenta = new JButton();
    JButton black = new JButton();
    DrawPanel drawPanel = new DrawPanel(this);

    private Color color = Color.BLACK;
    private JPanel panel;

    public Paint() {
        super("Доска для рисования");
        setSize(1000, 700);

        panel = (JPanel) getContentPane();
        //panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(BorderLayout.CENTER, drawPanel);

        JPanel south_panel = new JPanel();
        south_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        south_panel.setBackground(Color.pink);

        green.setBackground(Color.GREEN);
        red.setBackground(Color.RED);
        yellow.setBackground(Color.YELLOW);
        blue.setBackground(Color.BLUE);
        magenta.setBackground(Color.MAGENTA);
        black.setBackground(Color.BLACK);

        green.addActionListener(this);
        red.addActionListener(this);
        yellow.addActionListener(this);
        blue.addActionListener(this);
        magenta.addActionListener(this);
        black.addActionListener(this);

        south_panel.add(green);
        south_panel.add(red);
        south_panel.add(yellow);
        south_panel.add(blue);
        south_panel.add(magenta);
        south_panel.add(black);
        panel.add(BorderLayout.SOUTH, south_panel);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Paint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == green) {
            color = Color.GREEN;
        }
        else if (e.getSource() == red) {
            color = Color.RED;
        }
        else if (e.getSource() == yellow) {
            color = Color.YELLOW;
        }
        else if (e.getSource() == blue) {
            color = Color.BLUE;
        }
        else if (e.getSource() == magenta) {
            color = Color.MAGENTA;
        }
        else if (e.getSource() == black) {
            color = Color.BLACK;
        }
    }

    public Color getColor() {
        return color;
    }
}

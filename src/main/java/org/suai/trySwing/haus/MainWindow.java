package org.suai.trySwing.haus;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame{

    public MainWindow(String title) throws IOException {
        super(title);
        MyPanel panel = new MyPanel();
        Container c = getContentPane();
        c.add(panel);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        MainWindow mw = new MainWindow("Red rectangle");
    }
}

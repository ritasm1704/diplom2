package org.suai.trySwing.haus;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {

    boolean night = false;
    private Image im_haus;

    class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            night = !night;
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public MyPanel() throws IOException {
        loadImages("haus.png");
        MyMouseListener mm = new MyMouseListener();
        this.addMouseListener(mm);
    }

    void loadImages(String image) throws IOException {
        im_haus =ImageIO.read(new File(image));
    }

    public void paintComponent(Graphics g) {
        System.out.println("paintComponent");
        super.paintComponent(g);
        if (!night) {
            setBackground(new Color(34, 139, 34));
        }
        else {
            setBackground(new Color(47, 79, 79));
        }
        g.setColor(Color.red);
        g.drawRect(10, 30, 100, 200);
        g.drawRect(10, 30, 50, 100);
        g.drawImage(im_haus, 300, 150, null);
        g.drawRect(300, 150, 50, 50);
    }
}

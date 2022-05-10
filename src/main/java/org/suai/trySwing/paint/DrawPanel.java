package org.suai.trySwing.paint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel {

    private Graphics g;
    private BufferedImage image;
    private Paint paintFrame;
    private Graphics2D g2;
    private int x1, y1, x2, y2;

    public DrawPanel(Paint paintFrame) {
        this.paintFrame = paintFrame;
        setDoubleBuffered(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();

                if (g2 != null) {
                    g2.setColor(paintFrame.getColor());
                    g2.drawLine(x1, y1, x2, y2);
                    repaint();
                    x1 = x2;
                    y1 = y2;
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        //System.out.println("paintComponent");

        if (image == null) {
            image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            this.g2 = (Graphics2D) image.getGraphics();
            this.g2.setColor(Color.WHITE);
            this.g2.fillRect(0, 0, getWidth(), getHeight());

            this.g2.setColor(paintFrame.getColor());
            BasicStroke bs = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
            this.g2.setStroke(bs);
        }
        g.drawImage(image, 0, 0, null);
    }





}

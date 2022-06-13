package org.suai.view;

import org.suai.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class Arena extends JPanel {

    private InputComponent inputComponent;
    private ArenaModel arenaModel;
    private boolean isOver = false;

    private final int tileWidth = 10;
    private final int tileHeight = 10;
    private int mainPlayer = 0;

    class MyKeyInputHandler extends KeyAdapter {

        @Override
        public void  keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case (KeyEvent.VK_D) -> inputComponent.rightPressed = true;
                case (KeyEvent.VK_A) -> inputComponent.leftPressed = true;
                case (KeyEvent.VK_W) -> inputComponent.upPressed = true;
                case (KeyEvent.VK_S) -> inputComponent.downPressed = true;
                case (KeyEvent.VK_SPACE) -> inputComponent.space = true;
            }
        }
        @Override
        public void  keyReleased(KeyEvent e) {

            switch (e.getKeyCode()) {
                case (KeyEvent.VK_D) -> inputComponent.rightPressed = false;
                case (KeyEvent.VK_A) -> inputComponent.leftPressed = false;
                case (KeyEvent.VK_W) -> inputComponent.upPressed = false;
                case (KeyEvent.VK_S) -> inputComponent.downPressed = false;
                case (KeyEvent.VK_SPACE) -> inputComponent.space = false;
            }
        }
    }

    public Arena(ArenaModel arenaModel, int numberOfPlayer) {

        this.addKeyListener(new MyKeyInputHandler());
        setFocusable(true);
        inputComponent = new InputComponent(numberOfPlayer);
        this.arenaModel = arenaModel;
    }

    public void paintComponent(Graphics g) {
        //System.out.println("paintComponent");
        super.paintComponent(g);

        int[][] arena = arenaModel.getArenaAsMas();

        for (int i = 0; i < arena.length; i++) {
            for (int j = 0; j < arena[0].length; j++) {
                if (arena[i][j] == -1) {
                    g.setColor(Color.red);
                    g.fillRect(j*tileHeight, i*tileWidth, tileWidth, tileHeight);
                } else {
                    g.setColor(Color.green);
                    g.fillRect(j*tileHeight, i*tileWidth, tileWidth, tileHeight);
                }
            }
        }

        ArrayList<Player> players = arenaModel.getPlayers();

        /*int xH = 100;
        int yH = 20;
        int l = 200;*/

        for (int i = 0; i < players.size(); i++) {

            //System.out.println(players.get(i).getX() + " " + players.get(i).getY());
            if (!players.get(i).isDead) {

                int xHM = players.get(i).getX() * tileWidth - 5;
                int yHM = players.get(i).getY() * tileHeight - 10;
                int lM = 20;

                int tmpM = (int)(players.get(i).getHealth() / (players.get(i).getMaxHealth() * 0.01) * (lM * 0.01));
                g.setColor(new Color(14, 92, 38));
                g.fillRect(xHM, yHM, tmpM, 5);

                g.setColor(new Color(92, 14, 14));
                g.fillRect(xHM + tmpM, yHM, lM - tmpM, 5);

                g.setColor(Color.blue);
            }
            else {
                g.setColor(Color.white);
            }
            g.fillRect(players.get(i).getX()*tileWidth, players.get(i).getY()*tileHeight,
                    players.get(i).getWidth(), players.get(i).getHeight());

        }

        ArrayList<Monster> monsters = arenaModel.getMonsters();

        for (int i = 0; i < monsters.size(); i++) {
            if (!monsters.get(i).isDead) {
                int xHM = monsters.get(i).getX() * tileWidth - 5;
                int yHM = monsters.get(i).getY() * tileHeight - 10;
                int lM = 20;

                int tmpM = (int)(monsters.get(i).getHealth() / (monsters.get(i).getMaxHealth() * 0.01) * (lM * 0.01));
                g.setColor(new Color(14, 92, 38));
                g.fillRect(xHM, yHM, tmpM, 5);

                g.setColor(new Color(92, 14, 14));
                g.fillRect(xHM + tmpM, yHM, lM - tmpM, 5);

                g.setColor(Color.black);

                g.fillRect(monsters.get(i).getX()*tileHeight, monsters.get(i).getY()*tileWidth,
                        monsters.get(i).getWidth(), monsters.get(i).getHeight());
            }
        }

        ArrayList<Flower> flowers = arenaModel.getFlowers();

        for (int i = 0; i < flowers.size(); i++) {

            if (!flowers.get(i).isDead) {
                g.setColor(new Color(161, 105, 245));
                g.fillRect(flowers.get(i).getX()*tileHeight, flowers.get(i).getY()*tileWidth,
                        flowers.get(i).getWidth(), flowers.get(i).getHeight());
            }
        }
    }

    public int getNumber() {
        return inputComponent.numberOfPlayer;
    }

    public void update(ArenaModel arenaModel) {
        this.arenaModel = arenaModel;
        repaint();
    }

    public boolean getIsOver() {
        return isOver;
    }

    public InputComponent getInputComponent() {
        return inputComponent;
    }

}

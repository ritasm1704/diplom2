package org.suai.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Arena extends JPanel {

    private InputComponent inputComponent = new InputComponent();
    private ArenaModel arenaModel;
    private boolean isOver = false;

    private final int tileWidth = 10;
    private final int tileHeight = 10;

    class MyKeyInputHandler extends KeyAdapter {

        @Override
        public void  keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case (KeyEvent.VK_D) -> inputComponent.rightPressed = true;
                case (KeyEvent.VK_A) -> inputComponent.leftPressed = true;
                case (KeyEvent.VK_W) -> inputComponent.upPressed = true;
                case (KeyEvent.VK_S) -> inputComponent.downPressed = true;
            }
        }
        @Override
        public void  keyReleased(KeyEvent e) {

            switch (e.getKeyCode()) {
                case (KeyEvent.VK_D) -> inputComponent.rightPressed = false;
                case (KeyEvent.VK_A) -> inputComponent.leftPressed = false;
                case (KeyEvent.VK_W) -> inputComponent.upPressed = false;
                case (KeyEvent.VK_S) -> inputComponent.downPressed = false;
            }
        }
        @Override
        public void  keyTyped(KeyEvent e) {

        }
    }

    public Arena(String nameOfMap, int numberOfMonsters) {

        this.addKeyListener(new MyKeyInputHandler());
        setFocusable(true);
        arenaModel = new ArenaModel(nameOfMap, numberOfMonsters);
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

        for (int i = 0; i < players.size(); i++) {
            g.setColor(Color.blue);
            if (!players.get(i).isDead) {
                g.fillRect(players.get(i).getX()*tileHeight, players.get(i).getY()*tileWidth,
                        players.get(i).getWidth(), players.get(i).getHeight());
            }
        }

        ArrayList<Monster> monsters = arenaModel.getMonsters();
        boolean tmp = true;
        for (int i = 0; i < monsters.size(); i++) {
            g.setColor(Color.black);
            if (players.get(0).getX() != monsters.get(i).getX() || players.get(0).getY() != monsters.get(i).getY()) {
                tmp = false;

            }
            g.fillRect(monsters.get(i).getX()*tileHeight, monsters.get(i).getY()*tileWidth,
                    monsters.get(i).getWidth(), monsters.get(i).getHeight());
        }
        isOver = tmp;
    }

    public void update() {
        arenaModel.update(inputComponent);
        repaint();
    }

    public boolean getIsOver() {
        return isOver;
    }

}

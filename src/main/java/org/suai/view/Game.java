package org.suai.view;

import org.suai.model.Arena;
import org.suai.model.ArenaModel;
import org.suai.model.InputComponent;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {

    Arena arena;
    boolean isOver = false;
    float sum = 0;
    int count = 0;

    public Game(String title, int numberOfMonsters) {
        super(title);

        System.out.println("Creating Arena");
        //arena = new Arena(100,70);
        arena = new Arena(title, numberOfMonsters, false);
        Container c = getContentPane();
        c.add(arena);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 745);
        setVisible(true);
    }

    public float getSum() {
        return sum / count;
    }

    public void loop() {
        long lastTime = System.currentTimeMillis();
        long delta;

        int countFPS = 0;

        System.out.println("Starting loop");
        while (!isOver) {
            delta = System.currentTimeMillis() - lastTime;
            countFPS++;
            if (delta > 1000) {
                sum += (float) countFPS / delta;
                count += 1;
                countFPS = 0;
                lastTime = System.currentTimeMillis();
            }
            arena.update();
            //isOver = arena.getIsOver();
            //sOver = true;
        }
        this.dispose();
    }

    public static void main(String[] args) {
        Game game = new Game("map6.txt", 1);
        game.loop();
    }
}

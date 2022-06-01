package org.suai.controller.client;

import org.suai.controller.server.Connection;
import org.suai.model.ArenaModel;
import org.suai.view.Arena;
import org.suai.view.Panel1;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameClient extends JFrame {

    Arena arena;
    ArenaModel arenaModel;
    Client client;
    boolean isOver = false;

    public GameClient(String title, int numberOfMonsters) throws SocketException, UnknownHostException {
        super(title);
        System.out.println("Creating Game");
        arenaModel = new ArenaModel(title, numberOfMonsters, false);
        client = new Client(arenaModel, "localhost", 2000);
        client.start();

        add(new Panel1(this));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 745);
        setVisible(true);
    }

    public boolean sendGame(String message) throws IOException {
        return client.sendGame(message);
    }

    public int sendPassword(String message) throws IOException {
        return client.sendPassword(message);
    }

    public String[] getGames() throws IOException {
        return client.getGames();
    }

    public void createArena(int number) throws IOException {
        arena = new Arena(arenaModel, number);
        Container c = getContentPane();
        c.add(arena);
        setVisible(true);
        this.loop();
    }

    public void loop() throws IOException {
        System.out.println("Starting loop");

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (client.isRegistered) {
                    long lastTime = System.currentTimeMillis();
                    long delta;

                    while (!isOver) {

                        /*delta = System.currentTimeMillis() - lastTime;
                        if (delta > 1000) {
                            lastTime = System.currentTimeMillis();
                        }*/
                        try {
                            client.sendInputComponent(arena.getInputComponent());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        arena.update();
                        //isOver = arena.getIsOver();
                        //sOver = true;
                    }
                }
            }
        };
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        GameClient game = new GameClient("map6.txt", 1);
        //game.loop();
    }
}

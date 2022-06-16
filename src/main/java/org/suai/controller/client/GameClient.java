package org.suai.controller.client;

import org.suai.controller.server.Connection;
import org.suai.model.ArenaModel;
import org.suai.view.Arena;
import org.suai.view.Panel1;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameClient extends JFrame {

    Arena arena;
    ArenaModel arenaModel;
    boolean isOver = false;

    private String host = "192.168.0.108"; //192.168.0.108
    private int port = 2000;
    Client client;
    boolean broadcast = true;

    public GameClient(String title, int numberOfMonsters) throws SocketException, UnknownHostException {
        super(title);
        System.out.println("Creating Game: broadcast " + broadcast);
        arenaModel = new ArenaModel(title, numberOfMonsters, false);
        client = new Client(broadcast, arenaModel, host, port);
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

    public void setArenaModel(ArenaModel arenaModel) {
        this.arenaModel = arenaModel;
    }

    public void createArena(int number) throws IOException {
        arena = new Arena(arenaModel, number);
        client.setGameClient(this);
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
                        //System.out.println("update");
                        /*delta = System.currentTimeMillis() - lastTime;
                        if (delta > 1000) {
                            lastTime = System.currentTimeMillis();
                        }*/

                        try {
                            client.sendInputComponent(arena.getInputComponent());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        arena.update(arenaModel);
                        //isOver = arena.getIsOver();
                        //sOver = true;
                    }
                }
            }
        };
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        GameClient game = new GameClient("mapDemo.txt", 0);
        //game.loop();
    }
}

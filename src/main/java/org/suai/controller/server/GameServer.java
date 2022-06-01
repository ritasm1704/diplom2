package org.suai.controller.server;

import org.suai.model.ArenaModel;

import java.io.IOException;
import java.util.ArrayList;

public class GameServer extends Thread {

    private String name;
    private String password;
    Connection admin;
    ArrayList<Connection> connections = new ArrayList<>();
    int numberOfPlayers = 0;

    ArenaModel arenaModel;
    boolean isOver = false;

    public GameServer(String serverName, String password,Connection admin) {
        name = serverName;
        this.password = password;
        this.admin = admin;
        connections.add(admin);
        arenaModel = new ArenaModel("map6.txt", 1, false);
    }

    public void run() {
        long lastTime = System.currentTimeMillis();
        long delta;

        System.out.println("Starting loop");
        while (!isOver) {

            /*delta = System.currentTimeMillis() - lastTime;
            if (delta > 1000) {
                lastTime = System.currentTimeMillis();
            }*/
            for (int i = 0; i < connections.size(); i++) {
                arenaModel.update(connections.get(i).getInputComponent());
            }
            for (int i = 0; i < connections.size(); i++) {
                try {
                    connections.get(i).sendArena(arenaModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //isOver = arena.getIsOver();
            //sOver = true;
        }
    }

    public String getServerName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void addConnection(Connection connection) {

        connections.add(connection);
        numberOfPlayers++;
    }

}

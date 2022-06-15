package org.suai.controller.server;

import org.suai.model.ArenaModel;
import org.suai.model.InputComponent;

import java.io.IOException;
import java.util.ArrayList;

public class GameServer extends Thread {

    private String name;
    private String password;
    Connection admin;
    ArrayList<Connection> connections = new ArrayList<>();
    int numberOfPlayers = 0;

    private ArenaModel arenaModel;
    boolean isOver = false;

    public GameServer(String serverName, String password,Connection admin) {
        name = serverName;
        this.password = password;
        this.admin = admin;
        connections.add(admin);
        arenaModel = new ArenaModel("mapDemo.txt", 20, false);
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
                if (connections.get(i).getGameServer() == null) {
                    connections.get(i).setGameServer(this);
                }

                //System.out.println(connections.get(i).getInputComponent().rightPressed);
                //arenaModel.update(connections.get(i).getInputComponent());
            }
            for (int i = 0; i < connections.size(); i++) {
                try {
                    connections.get(i).sendArena(arenaModel);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //isOver = arena.getIsOver();
            //sOver = true;
        }
    }

    public void updateArenaModel(InputComponent inputComponent) {
        //System.out.println(inputComponent.rightPressed);
        arenaModel.update(inputComponent);
        //System.out.println(arenaModel.getPlayers().get(0).getX() + " " + arenaModel.getPlayers().get(0).getY());
    }

    public void addPlayer(int number) {
        arenaModel.addPlayer(number);
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

package org.suai.controller.server;

import org.suai.model.ArenaModel;
import org.suai.model.InputComponent;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GameServer extends Thread {

    private String name;
    private String password;
    Connection admin;
    ArrayList<Connection> connections = new ArrayList<>();
    int numberOfPlayers = 0;
    boolean broadcast;

    ServerOutput2 serverOutput2;
    private ArenaModel arenaModel;
    boolean isOver = false;

    public GameServer(boolean broadcast, int port, String serverName, String password, Connection admin) throws SocketException, UnknownHostException {
        this.broadcast = broadcast;
        name = serverName;
        this.password = password;
        this.admin = admin;
        connections.add(admin);
        arenaModel = new ArenaModel("mapDemo.txt", 20, false);
        if (broadcast) {
            serverOutput2 = new ServerOutput2(port);
            serverOutput2.setArenaModel(arenaModel);
            serverOutput2.start();
        }
    }

    public int getPort() {
        return serverOutput2.getPort();
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

            if (!broadcast) {
                for (int i = 0; i < connections.size(); i++) {
                    try {
                        connections.get(i).sendArena(arenaModel);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                serverOutput2.setArenaModel(arenaModel);
            }


            //isOver = arena.getIsOver();
            //sOver = true;
        }
    }

    /*public void addPort(int port) {
        serverOutput2.addPort(port);
    }*/

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

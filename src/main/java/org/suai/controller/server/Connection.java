package org.suai.controller.server;

import org.suai.model.ArenaModel;
import org.suai.model.InputComponent;
import org.suai.view.Arena;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Connection extends Thread {

    //private int port;
    //private InetAddress address;
    DatagramSocket serverSocket;

    private int portClient;
    private InetAddress addressClient;
    private GameServer gameServer;
    ServerInput serverInput;
    ServerOutput serverOutput;

    private InputComponent inputComponent;
    Server server;
    private boolean startGame = false;
    boolean broadcast;
    int broadcastPort;
    //ArrayList<GameServer> games;

    public Connection(boolean broadcast, int port, InetAddress addressClient, int portClient, Server server) throws UnknownHostException, SocketException {
        //address = InetAddress.getByName(host);
        //this.port = port;
        //this.gameServer = gameServer;
        this.broadcast = broadcast;
        serverSocket = new DatagramSocket(port);
        this.addressClient = addressClient;
        this.portClient = portClient;
        this.server = server;
        serverInput = new ServerInput(addressClient, port + 1);
        serverOutput = new ServerOutput(addressClient, port + 2);
        //System.out.println(addressClient);
        this.start();
    }

    @Override
    public void run() {
        try {
            //ServerInput serverInput = new ServerInput("localhost", 2003, 2002);
            while (true) {
                if (!startGame) {
                    DatagramPacket receivePacket = new DatagramPacket(new byte[1000], 1000);
                    serverSocket.receive(receivePacket);

                    String receiveString = new String(receivePacket.getData());
                    receiveString = receiveString.substring(0, receiveString.indexOf('\n'));
                    System.out.println("Client: " + receiveString);

                    if (receiveString.split(" ")[0].equals("newGame")) {
                        if (checkGame(receiveString.split(" ")[1])) {
                            GameServer gameServer = new GameServer(broadcast, server.getCountOfBroadcastPorts(), receiveString.split(" ")[1],
                                    receiveString.split(" ")[2], this);
                            gameServer.start();
                            server.setGameServer(gameServer);
                            this.gameServer = gameServer;
                            gameServer.addPlayer(0);
                            serverInput.setGameServer(this.gameServer);
                            serverInput.start();

                            String sendString = "OK";
                            System.out.println("Server: " + sendString);
                            byte[] buf = sendString.getBytes();
                            //System.out.println("Client: " + sendString);

                            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
                            serverSocket.send(sendPacket);

                            if (!broadcast) {
                                serverOutput.start();
                            } else {
                                //sendPortBroadcast(gameServer.getPort());
                            }
                        } else {
                            String sendString = "NO";
                            System.out.println("Server: " + sendString);
                            byte[] buf = sendString.getBytes();
                            //System.out.println("Client: " + sendString);

                            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
                            serverSocket.send(sendPacket);
                        }
                    } else if(receiveString.split(" ")[0].equals("sendMeGames")) {
                        String packGames = packGames();
                        byte[] buf = packGames.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
                        serverSocket.send(sendPacket);
                    } else if(receiveString.split(" ")[0].equals("checkGamePassword")) {
                        int res = checkGamePassword(receiveString.split(" ")[1], receiveString.split(" ")[2]);
                        if (res != -1) {
                            String sendString = "OK" + " " + res;
                            System.out.println("Server: " + sendString);
                            byte[] buf = sendString.getBytes();
                            //System.out.println("Client: " + sendString);

                            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
                            serverSocket.send(sendPacket);

                            if (!broadcast) {
                                serverOutput.start();
                            } else {
                                //sendPortBroadcast(gameServer.getPort());
                            }
                        } else {
                            String sendString = "NO ";
                            System.out.println("Server: " + sendString);
                            byte[] buf = sendString.getBytes();
                            //System.out.println("Client: " + sendString);

                            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
                            serverSocket.send(sendPacket);
                        }
                    }
                } else {
                    //inputComponent = serverInput.getInputComponent();

                }
                //System.out.println(new String(receivePacket.getData()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkGame(String name) {
        ArrayList<GameServer> games = server.getGames();
        for (int i = 0; i < games.size(); i++) {
            if (name.equals(games.get(i).getServerName())) {
                return false;
            }
        }
        inputComponent = new InputComponent(0);
        serverInput.setInputComponent(inputComponent);
        startGame = true;
        return true;
    }

    public int checkGamePassword(String name, String password) throws IOException {
        ArrayList<GameServer> games = server.getGames();
        for (int i = 0; i < games.size(); i++) {
            if (name.equals(games.get(i).getServerName())) {
                if (password.equals(games.get(i).getPassword())) {
                    games.get(i).addConnection(this);
                    startGame = true;
                    this.gameServer = games.get(i);
                    inputComponent = new InputComponent(games.get(i).numberOfPlayers);
                    gameServer.addPlayer(inputComponent.numberOfPlayer);
                    serverInput.setInputComponent(inputComponent);
                    serverInput.setGameServer(this.gameServer);
                    serverInput.start();

                    return inputComponent.numberOfPlayer;
                } else {
                    return -1;
                }
            }
        }
        return -1;
    }

    public String packGames() {
        StringBuilder res = new StringBuilder();
        ArrayList<GameServer> games = server.getGames();
        for (int i = 0; i < games.size(); i++) {
            res.append(games.get(i).getServerName()).append(",");
        }
        res.append("\n");
        return res.toString();
    }

    public InputComponent getInputComponent() {
        return serverInput.getInputComponent();
    }
/*
    public void sendPortBroadcast(int port) throws IOException {
        String sendString = "port " + port + "\n";
        System.out.println("Server: " + sendString);
        byte[] buf = sendString.getBytes();
        //System.out.println("Client: " + sendString);

        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
        serverSocket.send(sendPacket);
    }*/


    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void sendArena(ArenaModel arenaModel) throws IOException, InterruptedException {
        serverOutput.setArenaModel(arenaModel);
    }
}

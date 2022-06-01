package org.suai.controller.server;

import org.suai.model.ArenaModel;
import org.suai.model.InputComponent;
import org.suai.view.Arena;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Connection extends Thread {

    //private int port;
    //private InetAddress address;
    DatagramSocket serverSocket;

    private int portClient;
    private InetAddress addressClient;

    private InputComponent inputComponent;
    Server server;
    private boolean startGame = false;
    //ArrayList<GameServer> games;

    public Connection(int port, InetAddress addressClient, int portClient, Server server) throws UnknownHostException, SocketException {
        //address = InetAddress.getByName(host);
        //this.port = port;
        serverSocket = new DatagramSocket(port);
        this.addressClient = addressClient;
        this.portClient = portClient;
        this.server = server;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!startGame) {
                    DatagramPacket receivePacket = new DatagramPacket(new byte[1000], 1000);
                    serverSocket.receive(receivePacket);

                    String receiveString = new String(receivePacket.getData());
                    receiveString = receiveString.substring(0, receiveString.indexOf('\n'));
                    System.out.println("Client: " + receiveString);

                    if (receiveString.split(" ")[0].equals("newGame")) {
                        if (checkGame(receiveString.split(" ")[1])) {
                            GameServer gameServer = new GameServer(receiveString.split(" ")[1],
                                    receiveString.split(" ")[2], this);
                            gameServer.start();
                            server.setGameServer(gameServer);
                            String sendString = "OK";
                            System.out.println("Server: " + sendString);
                            byte[] buf = sendString.getBytes();
                            //System.out.println("Client: " + sendString);

                            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addressClient, portClient);
                            serverSocket.send(sendPacket);
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
                    System.out.println("Server receive");
                    DatagramPacket receivePacket = new DatagramPacket(new byte[139], 139);
                    serverSocket.receive(receivePacket);
                    ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    inputComponent = (InputComponent) ois.readObject();
                }
                //System.out.println(new String(receivePacket.getData()));
            }
        } catch (IOException | ClassNotFoundException e) {
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
        startGame = true;
        return true;
    }

    public int checkGamePassword(String name, String password) {
        ArrayList<GameServer> games = server.getGames();
        for (int i = 0; i < games.size(); i++) {
            if (name.equals(games.get(i).getServerName())) {
                if (password.equals(games.get(i).getPassword())) {
                    games.get(i).addConnection(this);
                    startGame = true;
                    inputComponent = new InputComponent(games.get(i).numberOfPlayers);
                    return games.get(i).numberOfPlayers;
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
        return inputComponent;
    }

    public void sendArena(ArenaModel arenaModel) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(arenaModel);
        oos.flush();

        byte[] buf = baos.toByteArray();;
        System.out.println("Arena size: " + buf.length);

        DatagramPacket pack = new DatagramPacket(buf, buf.length, addressClient, portClient);
        serverSocket.send(pack);
        serverSocket.close();
    }
}

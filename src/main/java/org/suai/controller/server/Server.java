package org.suai.controller.server;

import org.suai.model.InputComponent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    DatagramSocket serverSocket;
    int countOfPorts;
    Thread thread;

    private ArrayList<GameServer> games = new ArrayList<>();
    private ArrayList<Connection> connections = new ArrayList<>();


    public Server(int port) throws SocketException {
        serverSocket = new DatagramSocket(port);
        countOfPorts = port + 1;
        //Server server = this;

    }

    public void start() {
        System.out.println("----Server is ready----");
        try {
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(new byte[11], 11);
                serverSocket.receive(receivePacket);

                String receiveString = new String(receivePacket.getData());
                System.out.println("Client: " + receiveString);
                if (receiveString.equals("knock_knock")) {

                    String sendString = "port " + countOfPorts + "\n";
                    connections.add(new Connection(countOfPorts, receivePacket.getAddress(), receivePacket.getPort(), this));
                    countOfPorts++;

                    byte[] sendData = sendString.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                    serverSocket.send(sendPacket);

                    System.out.println("Server: " + sendString);
                }
                //System.out.println(new String(receivePacket.getData()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<GameServer> getGames() {
        return games;
    }

    public void setGameServer(GameServer game) {
        games.add(game);
    }

    public static void main(String[] args) throws SocketException {
        Server server = new Server(2000);
        server.start();
    }


}

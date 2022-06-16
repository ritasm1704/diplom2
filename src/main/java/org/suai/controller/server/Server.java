package org.suai.controller.server;

import org.suai.model.InputComponent;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

    DatagramSocket serverSocket;
    int countOfPorts;
    int countOfBroadcastPorts;
    boolean broadcast = true;
    Thread thread;

    private ArrayList<GameServer> games = new ArrayList<>();
    private ArrayList<Connection> connections = new ArrayList<>();


    public Server(int port) throws IOException {
        serverSocket = new DatagramSocket(port);
        countOfPorts = port + 1;
        countOfBroadcastPorts = port + 500;
        //Server server = this;
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine(); //you get the IP as a String
        System.out.println(ip);
        //System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

    public void start() {
        System.out.println("----Server is ready----");
        System.out.println("----broadcast " + broadcast +"----");
        try {
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(new byte[11], 11);
                serverSocket.receive(receivePacket);

                String receiveString = new String(receivePacket.getData());
                System.out.println("Client: " + receiveString);
                if (receiveString.equals("knock_knock")) {

                    String sendString = "port " + countOfPorts + "\n";
                    connections.add(new Connection(broadcast, countOfPorts, receivePacket.getAddress(), receivePacket.getPort(), this));
                    countOfPorts += 3;

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

    public int getCountOfBroadcastPorts() {
        countOfBroadcastPorts++;
        return countOfBroadcastPorts;
    }

    public ArrayList<GameServer> getGames() {
        return games;
    }

    public void setGameServer(GameServer game) {
        games.add(game);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(2000);
        server.start();
    }


}

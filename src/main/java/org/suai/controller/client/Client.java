package org.suai.controller.client;

import org.suai.model.ArenaModel;
import org.suai.model.InputComponent;
import org.suai.view.Arena;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Client extends Thread {

    private ArenaModel arenaModel;

    private InetAddress address;
    private int port;
    private int serverInputPort = 0;
    private int serverOutputPort = 0;

    boolean isRegistered = false;

    DatagramSocket clientSocket = new DatagramSocket();
    ClientOutput clientOutput;
    ClientInput clientInput;
    boolean broadcast;

    public Client(boolean broadcast, ArenaModel arenaModel, String host, int port) throws SocketException, UnknownHostException {
        this.arenaModel = arenaModel;
        this.broadcast = broadcast;
        address = InetAddress.getByName(host);
        this.port = port;
    }

    @Override
    public void run() {

        try {
            String sendString = "knock_knock";
            System.out.println("Client: " + sendString);
            byte[] sendData = sendString.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(new byte[100], 100);
            clientSocket.receive(receivePacket);

            String initiationString = new String(receivePacket.getData());
            initiationString = initiationString.substring(0, initiationString.indexOf('\n'));
            System.out.println("Server: " + initiationString);
            port = Integer.parseInt(initiationString.split(" ")[1]);
            serverInputPort = port + 1;
            serverOutputPort = port + 2;
            clientOutput = new ClientOutput(address, new DatagramSocket(), serverInputPort);
            if (!broadcast) {
                clientInput = new ClientInput(broadcast, address, new DatagramSocket(), serverOutputPort);
            } else {
                clientInput = new ClientInput(broadcast, address, new DatagramSocket(4000), 0);
            }

            /*while (true) {
                //System.out.println("is Registered " + isRegistered);
                if (isRegistered) {
                    System.out.println("is Registered " + isRegistered);
                    //arenaModel = clientInput.getArenaModel();
                }
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArenaModel getArenaModel() {
        return arenaModel;
    }

    public boolean sendGame(String string) throws IOException {

        byte[] buf = string.getBytes();
        System.out.println("Client: " + string);

        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(new byte[2], 2);
        clientSocket.receive(receivePacket);

        String receiveString = new String(receivePacket.getData());
        System.out.println("Server: " + receiveString);
        if (receiveString.equals("OK")) {

            isRegistered = true;
            System.out.println("Client start " + isRegistered);

            clientOutput.start();
            clientInput.start();
            //System.out.println("is Registered " + isRegistered);
            return true;
        } else {
            return false;
        }
    }

    public int sendPassword(String string) throws IOException {

        byte[] buf = string.getBytes();
        System.out.println("Client: " + string);

        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(new byte[4], 4);
        clientSocket.receive(receivePacket);

        String receiveString = new String(receivePacket.getData());
        System.out.println("Server: " + receiveString);
        if (receiveString.split(" ")[0].equals("OK")) {
            isRegistered = true;
            System.out.println("Client start" + isRegistered);

            clientOutput.start();
            clientInput.start();
            //System.out.println("is Registered " + isRegistered);
            return Integer.parseInt(receiveString.split(" ")[1]);
        } else {
            return -1;
        }
    }

    public String[] getGames() throws IOException {
        String string = "sendMeGames\n";
        byte[] buf = string.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, port);
        clientSocket.send(sendPacket);

        DatagramPacket receivePacket = new DatagramPacket(new byte[1000], 1000);
        clientSocket.receive(receivePacket);

        String receiveString = new String(receivePacket.getData());
        receiveString = receiveString.split("\n") [0];
        return receiveString.split(",");
    }

    public void sendInputComponent(InputComponent inputComponent) throws IOException {

        clientOutput.setInputComponent(inputComponent);
    }

    public void setGameClient(GameClient gameClient) {
        clientInput.setGameClient(gameClient);
    }

    public int getServerInputPort() {
        return serverInputPort;
    }

    public int getPort() {
        return port;
    }
}

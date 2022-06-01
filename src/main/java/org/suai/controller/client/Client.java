package org.suai.controller.client;

import org.suai.model.ArenaModel;
import org.suai.model.InputComponent;
import org.suai.view.Arena;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Client extends Thread {

    Arena arena;
    ArenaModel arenaModel;

    private InetAddress address;
    private int port;

    boolean isRegistered = false;

    DatagramSocket clientSocket = new DatagramSocket();

    public Client(ArenaModel arenaModel, String host, int port) throws SocketException, UnknownHostException {
        this.arenaModel = arenaModel;

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

            while (true) {
                System.out.println("is Registered " + isRegistered);
                if (isRegistered) {
                    System.out.println("Client receive");
                    DatagramPacket pack = new DatagramPacket(new byte[58808], 58808);
                    clientSocket.receive(pack);

                    ByteArrayInputStream bais = new ByteArrayInputStream(pack.getData());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    arenaModel = (ArenaModel) ois.readObject();

                }
            }

        } catch (IOException | ClassNotFoundException e) {
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(inputComponent);
        oos.flush();

        byte[] buf = baos.toByteArray();
        //System.out.println("Input component size: " + buf.length);

        DatagramPacket pack = new DatagramPacket(buf, buf.length, address, port);
        clientSocket.send(pack);
    }
}

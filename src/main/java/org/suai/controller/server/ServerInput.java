package org.suai.controller.server;

import org.suai.model.InputComponent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class ServerInput extends Thread {

    private int portClient = 0;
    private InetAddress address;
    private DatagramSocket serverSocket;
    private InputComponent inputComponent = null;
    private GameServer gameServer;

    public ServerInput(InetAddress address, int portServer) throws SocketException, UnknownHostException {
        serverSocket = new DatagramSocket(portServer);
        //this.portClient = portClient;
        this.address = address;
    }

    @Override
    public void run() {
        DatagramPacket receivePacketPort = new DatagramPacket(new byte[100], 100);
        try {
            serverSocket.receive(receivePacketPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String stringPort = new String(receivePacketPort.getData());
        //System.out.println(string.substring(0, string.indexOf("\n")));
        if (stringPort.substring(0, stringPort.indexOf("\n")).equals("OK")) {
            portClient = receivePacketPort.getPort();
            System.out.println("Server Input: Client port: " + portClient);
        }

        while (true) {
            //System.out.println("Receive Input");
            //int a = 0;
            DatagramPacket receivePacket = new DatagramPacket(new byte[100], 100);
            try {
                serverSocket.receive(receivePacket);
                String string = new String(receivePacket.getData());
                //System.out.println(string.substring(0, string.indexOf("\n")));
                int length = Integer.parseInt(string.substring(0, string.indexOf("\n")));

                string = "OK\n";
                byte[] bufStr = string.getBytes();
                //System.out.println("Input component size: " + buf.length);
                DatagramPacket pack = new DatagramPacket(bufStr, bufStr.length, address, portClient);
                serverSocket.send(pack);

                receivePacket = new DatagramPacket(new byte[length], length);
                serverSocket.receive(receivePacket);

                ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                inputComponent = (InputComponent) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            //System.out.println("update arena " + inputComponent.numberOfPlayer);
            if (inputComponent != null) {
                //System.out.println("update arena " + inputComponent.numberOfPlayer + " not null");
                gameServer.updateArenaModel(inputComponent);
            }
        }
    }

    public void setGameServer(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void setInputComponent(InputComponent inputComponent) {
        this.inputComponent = inputComponent;
    }

    public InputComponent getInputComponent() {
        return inputComponent;
    }
}

package org.suai.controller.client;

import org.suai.model.ArenaModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientInput extends Thread {

    private int portServer;
    private InetAddress address;
    private DatagramSocket clientSocket;
    //private ArenaModel arenaModel;
    private GameClient gameClient;
    boolean broadcast;

    public ClientInput(boolean broadcast, InetAddress address, DatagramSocket clientSocket, int portServer) {
        this.clientSocket = clientSocket;
        this.portServer = portServer;
        this.address = address;
        this.broadcast = broadcast;
    }

    /*public ArenaModel getArenaModel() {
        return arenaModel;
    }*/

    public void setPortServer(int port) {
        portServer = port;
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void run() {

        if (!broadcast) {
            String strOk = "OK\n";
            byte[] bufStrOk = strOk.getBytes();
            DatagramPacket packPort = new DatagramPacket(bufStrOk, bufStrOk.length, address, portServer);
            try {
                clientSocket.send(packPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //System.out.println(clientSocket.getPort());

        while (true) {

            try {
                DatagramPacket receivePacket = new DatagramPacket(new byte[8], 8);
                clientSocket.receive(receivePacket);

                String receiveString = new String(receivePacket.getData());
                //System.out.println("00Server: " + receiveString);

                if (receiveString.equals("newArena")) {

                    ByteArrayOutputStream mainBuf = new ByteArrayOutputStream();

                    while (true) {
                        receivePacket = new DatagramPacket(new byte[4], 4);
                        clientSocket.receive(receivePacket);
                        receiveString = new String(receivePacket.getData());
                        //System.out.println(receiveString);
                        if (receiveString.equals("END0")) {

                            break;
                        }
                        if (receiveString.equals("1000")) {
                            receivePacket = new DatagramPacket(new byte[1000], 1000);
                            clientSocket.receive(receivePacket);
                            mainBuf.write(receivePacket.getData());

                        } else {
                            //System.out.println(receiveString);
                            if (receiveString.equals("newA")) {
                                System.out.println(receiveString);
                                break;
                            }
                            int size = Integer.parseInt(receiveString);
                            receivePacket = new DatagramPacket(new byte[size], size);
                            clientSocket.receive(receivePacket);
                            mainBuf.write(receivePacket.getData());
                        }
                    }

                    ByteArrayInputStream bais = new ByteArrayInputStream(mainBuf.toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    ArenaModel arenaModel = (ArenaModel) ois.readObject();
                    gameClient.setArenaModel(arenaModel);
                    if (arenaModel != null) {
                        //System.out.println(arenaModel.getPlayers().get(0).getX() + " " + arenaModel.getPlayers().get(0).getY());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

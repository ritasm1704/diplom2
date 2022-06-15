package org.suai.controller.server;

import org.suai.model.ArenaModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class ServerOutput extends Thread {

    private int portClient = 0;
    private InetAddress address;
    private DatagramSocket serverSocket;
    private ArenaModel arenaModel;

    public ServerOutput(InetAddress address, int portServer) throws SocketException {
        serverSocket = new DatagramSocket(portServer);
        //this.portClient = portClient;
        this.address = address;
    }

    public void setArenaModel(ArenaModel arenaModel) {
        this.arenaModel = arenaModel;
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
            System.out.println("Client port: " + portClient);
        }


        while (true) {

            try {
                if (arenaModel != null) {
                    String sendString = "newArena\n";
                    //System.out.println("Server: " + sendString);
                    byte[] buf = sendString.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, address, portClient);
                    serverSocket.send(sendPacket);
                    //sleep(1);

                    //System.out.println(string.substring(0, string.indexOf("\n")));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(arenaModel);
                    oos.flush();

                    buf = baos.toByteArray();
                    int bufLength = buf.length;
                    int count = 0;
                    //int countOfPacks = 0;
                    //System.out.println("Arena size: " + buf.length);
                    while (count + 1000 < bufLength) {
                        //countOfPacks++;
                        //System.out.println(countOfPacks + "/" + bufLength/1000);
                        String sendString2 = "1000";
                        //System.out.println("Server: " + sendString2);
                        byte[] buf2 = sendString2.getBytes();

                        DatagramPacket sendPacket2 = new DatagramPacket(buf2, buf2.length, address, portClient);
                        serverSocket.send(sendPacket2);

                        sleep(1);

                        byte[] newBuf = Arrays.copyOfRange(buf, count, count + 1000);
                        sendPacket2 = new DatagramPacket(newBuf, newBuf.length, address, portClient);
                        serverSocket.send(sendPacket2);

                        //sleep(1);

                        count += 1000;
                        //System.out.println(string.substring(0, string.indexOf("\n")));
                    }

                    if (count != bufLength) {
                        //System.out.println("count != bufLength - 1");
                        int length = bufLength - count;
                        //System.out.println(length);
                        StringBuilder sendString2 = new StringBuilder(length + "");
                        //System.out.println(sendString2);
                        while (sendString2.length() < 4) {
                            sendString2.insert(0, "0");
                        }
                        //System.out.println(sendString2);
                        byte[] buf2 = sendString2.toString().getBytes();

                        DatagramPacket sendPacket2 = new DatagramPacket(buf2, buf2.length, address, portClient);
                        serverSocket.send(sendPacket2);

                        sleep(1);

                        byte[] newBuf = Arrays.copyOfRange(buf, count, count + length);
                        DatagramPacket pack = new DatagramPacket(newBuf, newBuf.length, address, portClient);
                        serverSocket.send(pack);

                        //sleep(1);
                    }

                    String sendString2 = "END0";
                    byte[] buf2 = sendString2.getBytes();

                    DatagramPacket sendPacket2 = new DatagramPacket(buf2, buf2.length, address, portClient);
                    serverSocket.send(sendPacket2);

                    sleep(1);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

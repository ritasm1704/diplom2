package org.suai.controller.server;

import org.suai.model.ArenaModel;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class ServerOutput2 extends Thread{

    private int portClient = 6000;
    //private InetAddress address;
    private InetAddress broadcastAddress;
    private DatagramSocket serverSocket;
    private ArenaModel arenaModel;
    private int serverPort;

    public ServerOutput2(int port) throws SocketException, UnknownHostException {
        serverPort = port;
        serverSocket = new DatagramSocket();
        //this.portClient = portClient;
        //this.address = address;
        //System.out.println(serverSocket.getPort());

        broadcastAddress = InetAddress.getByName("192.168.0.255");

        try {
            serverSocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void setArenaModel(ArenaModel arenaModel) {
        this.arenaModel = arenaModel;
    }

    public int getPort() {
        return serverPort;
    }

    @Override
    public void run() {
        while (true) {
            //System.out.println("while");

            try {
                if (arenaModel != null) {
                    String sendString = "newArena\n";
                    //System.out.println("Server: " + sendString);
                    byte[] buf = sendString.getBytes();
                    int count2 = buf.length;
                    DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, broadcastAddress, portClient);
                    serverSocket.send(sendPacket);
                    sleep(1);

                    //System.out.println(string.substring(0, string.indexOf("\n")));

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(arenaModel);
                    oos.flush();

                    buf = baos.toByteArray();
                    //count2 += buf.length;
                    //System.out.println(count2);

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
                        count2 += buf2.length + 1000;
                        DatagramPacket sendPacket2 = new DatagramPacket(buf2, buf2.length, broadcastAddress, portClient);
                        serverSocket.send(sendPacket2);

                        sleep(1);

                        byte[] newBuf = Arrays.copyOfRange(buf, count, count + 1000);
                        sendPacket2 = new DatagramPacket(newBuf, newBuf.length, broadcastAddress, portClient);
                        serverSocket.send(sendPacket2);

                        sleep(1);

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
                        count2 += buf2.length + length;

                        DatagramPacket sendPacket2 = new DatagramPacket(buf2, buf2.length, broadcastAddress, portClient);
                        serverSocket.send(sendPacket2);

                        sleep(1);

                        byte[] newBuf = Arrays.copyOfRange(buf, count, count + length);
                        DatagramPacket pack = new DatagramPacket(newBuf, newBuf.length, broadcastAddress, portClient);
                        serverSocket.send(pack);

                        sleep(1);
                    }

                    String sendString2 = "END0";
                    byte[] buf2 = sendString2.getBytes();
                    count2 += buf2.length;
                    DatagramPacket sendPacket2 = new DatagramPacket(buf2, buf2.length, broadcastAddress, portClient);
                    serverSocket.send(sendPacket2);

                    sleep(12);
                    //System.out.println(count2);
                    //break;
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

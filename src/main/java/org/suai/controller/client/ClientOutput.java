package org.suai.controller.client;

import org.suai.model.InputComponent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class ClientOutput extends Thread {

    private int portServer;
    private InetAddress address;
    private DatagramSocket clientSocket;
    private InputComponent inputComponent;

    public ClientOutput(InetAddress address, DatagramSocket clientSocket, int portServer) throws SocketException, UnknownHostException {
        //clientSocket = new DatagramSocket(portClient);
        this.clientSocket = clientSocket;
        this.portServer = portServer;
        this.address = address;
    }

    @Override
    public void run() {

        String portCl = "OK\n";
        byte[] bufPort = portCl.getBytes();
        DatagramPacket packPort = new DatagramPacket(bufPort, bufPort.length, address, portServer);
        try {
            clientSocket.send(packPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            //System.out.println("Send Input");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                if (inputComponent != null) {
                    //System.out.println("Send Input");

                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(inputComponent);
                    oos.flush();

                    byte[] buf = baos.toByteArray();

                    String string = buf.length + "\n";
                    byte[] bufStr = string.getBytes();
                    //System.out.println("Input component size: " + buf.length);
                    DatagramPacket pack = new DatagramPacket(bufStr, bufStr.length, address, portServer);
                    clientSocket.send(pack);

                    DatagramPacket receivePacket = new DatagramPacket(new byte[1000], 1000);
                    clientSocket.receive(receivePacket);

                    String receiveString = new String(receivePacket.getData());
                    receiveString = receiveString.substring(0, receiveString.indexOf("\n"));

                    if (receiveString.equals("OK")) {
                        pack = new DatagramPacket(buf, buf.length, address, portServer);
                        clientSocket.send(pack);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setInputComponent(InputComponent inputComponent) {
        this.inputComponent = inputComponent;
    }
}

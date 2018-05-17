package sample.CommunicationHandler;

import sample.Model.Peer;
import sample.Model.Post;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReaderThread extends  Thread {
    DatagramSocket socket = null;


    //This thread read the UDP socket and take peers
    public ReaderThread(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {

        while(true) {
            Thread.currentThread().setPriority(8);
            byte[] incomingData = new byte[30*1024];

            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            System.out.println("Ready to accept a packet");
            try {
                socket.receive(incomingPacket);
                System.out.println("Received a packet");
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(in));
                InetAddress sender_IPAddress = incomingPacket.getAddress();//holds the sender details
                int sender_port = incomingPacket.getPort();
                try {
                    Object obj=is.readObject();
                    ReceivedPacketHandler pktHandler=new ReceivedPacketHandler(obj,sender_IPAddress,sender_port);
                    System.out.println("Packet is handed over to the packet handler");
                    is.close();
                    pktHandler.start();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


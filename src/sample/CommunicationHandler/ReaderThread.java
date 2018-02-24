package sample.CommunicationHandler;

import sample.Model.Post;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ReaderThread extends  Thread {
    DatagramSocket socket = null;
    byte[] incomingData = new byte[1024];

    public ReaderThread(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        while(true) {
            byte[] incomingData = new byte[1024];
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            System.out.println("Ready to accept a packet");
            try {
                socket.receive(incomingPacket);
                System.out.println("A received a packet");
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                InetAddress sender_IPAddress = incomingPacket.getAddress();//holds the sender details
                int sender_port = incomingPacket.getPort();
                try {
                    Post post = (Post) is.readObject();
                    System.out.println("Post object received = " + post);
                    ReceivedPacketHandler pktHandler=new ReceivedPacketHandler(post,sender_IPAddress,sender_port);
                    pktHandler.start();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String reply = "Thank you for the message";
                byte[] replyBytea = reply.getBytes();
                DatagramPacket replyPacket =
                        new DatagramPacket(replyBytea, replyBytea.length, sender_IPAddress, sender_port);
                socket.send(replyPacket);
                Thread.sleep(2000);//no need
                //System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


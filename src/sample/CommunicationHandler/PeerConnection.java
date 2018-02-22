package sample.CommunicationHandler;
import javafx.geometry.Pos;
import sample.Model.Post;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.*;



//a udp communication handler
public class PeerConnection {
    DatagramSocket socket = null;

    public void createTheSocketListner() {
        try {
            socket = new DatagramSocket(9875);//use the registerd port in BS
            //make a therad to listen forever from this port.this should be the registerd por of peer
            ReaderThread readerThread = new ReaderThread(socket);
            readerThread.start();

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public void sendViaSocket() {
        try {

            DatagramSocket Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] incomingData = new byte[1024];
            Post student = new Post("Dinushi123", "I am not well my Friends");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(student);
            byte[] data = outputStream.toByteArray();
            //use the dedicated socket to send data...
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9875);
            Socket.send(sendPacket);
            System.out.println("Message sent from client");
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            Socket.receive(incomingPacket);
            String response = new String(incomingPacket.getData());
            System.out.println("Response from server:" + response);
            Thread.sleep(2000);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

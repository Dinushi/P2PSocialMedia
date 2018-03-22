package sample.CommunicationHandler;
import javafx.geometry.Pos;
import sample.Model.Message;
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
    public static int myPort;
//1----listen 9877 send 9876
    //next run listen 9876 send 9877
    public void createTheSocketListner(int port) {
        try {
            myPort=port;
            //socket = new DatagramSocket(9877);//use the registerd port in BS
            socket = new DatagramSocket(port);//use the registerd port in BS
            //socket = new DatagramSocket(9877);
            System.out.println("Spcket is started");
            //make a therad to listen forever from this port.this should be the registerd por of peer
            ReaderThread readerThread = new ReaderThread(socket);
            System.out.println("Thread create");
            readerThread.start();
            System.out.println("Thread started");

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public void clodeThesocket(){
        socket.disconnect();
    }

    public void sendViaSocket(Object obj) {
        try {

            DatagramSocket Socket = new DatagramSocket();//this should be sent using the same port which t listens
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] incomingData = new byte[1024];
            //Post student = new Post("Dinushi123", "I am not well my Friends");

            //System.out.println("new post is created");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            System.out.println("output stream ok");

            if (obj instanceof Post) {
                Post post=(Post)obj;
                os.writeObject(post);
            }else if(obj instanceof Message){
                Message msg=(Message)obj;
                os.writeObject(msg);
            }
            //os.writeObject(student);
            //os.writeObject(post);
            byte[] data = outputStream.toByteArray();
            //use the dedicated socket to send data...
            System.out.println("ready tos send to the port");
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);

            Socket.send(sendPacket);
            System.out.println("Post sent");
            //DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            //Socket.receive(incomingPacket);
            //String response = new String(incomingPacket.getData());
            //System.out.println("Response from server:" + response);
            //Thread.sleep(2000);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        //} catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

}

package sample.CommunicationHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class HeartbeatAgent extends Thread {
    private InetAddress peerIp;
    private int peerPort;

    public HeartbeatAgent(InetAddress ip, int port){
        this.peerIp=ip;
        this.peerPort=port;
    }
    public void run() {
        try{
            //loop until it receives a reply packet from the other end
            DatagramSocket Socket = new DatagramSocket();//this should be sent using the same port which t listens//now using any port
            InetAddress IPAddress = InetAddress.getByName("localhost");//for now ip is always localhost
            byte[] incomingData = new byte[1024];

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            System.out.println("output stream ok");
            os.writeObject("isOnline");

            byte[] data = outputStream.toByteArray();
            System.out.println("ready tos send a heartbeat");
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, peerIp,peerPort);

            Socket.send(sendPacket);
            System.out.println("heartbeat sent");
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

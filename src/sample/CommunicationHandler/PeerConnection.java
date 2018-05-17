package sample.CommunicationHandler;
import javafx.geometry.Pos;
import sample.Model.*;

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
import java.time.LocalDateTime;
import java.util.ArrayList;


//a udp communication handler
public class PeerConnection {
    private static PeerConnection peerConn=null;
    public static boolean initialLogin=false;
    private static int current_msgSeqNum=0;
    private static int current_convSeqNum=0;
    private PeerConnection(){
    }

    public static PeerConnection getPeerConnection(){
        if(peerConn==null) {
            peerConn = new PeerConnection();
            return peerConn;
        }else{
            return peerConn;
        }
    }
    DatagramSocket socket = null;

    public void createTheSocketListner() {
        try {
            //myPort=port;
            //socket = new DatagramSocket(9877);//use the registerd port in BS
            System.out.println("MyIP"+Owner.myIP);
            System.out.println("MyPort"+Owner.myPort);
            this.socket = new DatagramSocket(Owner.myPort,Owner.myIP);//use the registerd port in BS
            //socket = new DatagramSocket(9877);
            System.out.println("Socket is started");
            //make a therad to listen forever from this port.this should be the registerd por of peer
            ReaderThread readerThread = new ReaderThread(socket);
            System.out.println("Thread create");
            readerThread.start();
            System.out.println("Thread started");

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public void closeThesocket(){
        socket.disconnect();
    }

    public void sendViaSocket(Object obj, ArrayList<ReceivingPeer> peerIP_ports) {//these will bring the ips of peers to sent
        System.out.println("Came to send via socket");
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            System.out.println("output stream ok");

            if (obj instanceof Post) {
                Post post=(Post)obj;
                System.out.println("write to send a Post");
                os.writeObject(post);
            }else if(obj instanceof Message) {
                Message msg = (Message) obj;
                //this value is set to indicate the seq of packet sending in order to acheive reliability
                msg.setUDPSeqNum(String.valueOf(current_msgSeqNum));
                msg.setSentTimeInMillis(System.currentTimeMillis());
                current_msgSeqNum++;

                System.out.println("write to send a Message");
                os.writeObject(msg);
                if(current_msgSeqNum==1){
                    //start the thread after sending the first message
                    MessageRetransmitter.getMessageRetransmitter().start();
                }
                MessageRetransmitter.getMessageRetransmitter().addAMessage(msg,peerIP_ports);

            }else if(obj instanceof Conversation){
                Conversation conv=(Conversation)obj;

                conv.setUDPSeqNum(String.valueOf(current_convSeqNum));//initially set to 0
                conv.setSentTimeOfConversationinMillis(System.currentTimeMillis());
                current_convSeqNum++;

                System.out.println("write to send a Conversation");
                os.writeObject(conv);

                if(current_convSeqNum==1){
                    //start the thread after sending the first message
                    ConversationRetransmitter.getConversationRetransmitter().start();
                }
                ConversationRetransmitter.getConversationRetransmitter().addAConversation(conv,peerIP_ports);

            }else if(obj instanceof DiscoverdPeer){
                DiscoverdPeer joinReq=(DiscoverdPeer) obj;
                System.out.println("Write to send  a Dis_peer##################################");
                os.writeObject(joinReq);
            }else if(obj instanceof Peer){
                Peer peer=(Peer) obj;
                System.out.println("Peer Object that i sent"+peer.getUsername()+""+peer.getIp()+""+peer.getPort());
                System.out.println("Write to send A peer");
                os.writeObject(peer);

            }else if(obj instanceof String){
                String str=(String) obj;
                System.out.println("Write to send A String");
                os.writeObject(str);
            }else if(obj instanceof LocalDateTime){
                LocalDateTime date=(LocalDateTime) obj;
                System.out.println("Write to send LastlogOutTime");
                os.writeObject(date);
            }else if(obj instanceof Reply){
                Reply reply=(Reply) obj;
                System.out.println("Write to send a reply");
                os.writeObject(reply);
            }

            byte[] data = outputStream.toByteArray();
            //use the dedicated socket to send data...

            System.out.println("ready tos send to the port");
            if(peerIP_ports!=null){
                //Ip list should not be null
                if(!peerIP_ports.isEmpty()){
                    //Ip list should not be empty
                    for (ReceivingPeer receiver : peerIP_ports) {
                        DatagramPacket sendPacket = new DatagramPacket(data, data.length, receiver.getIP(), receiver.getPort());

                        this.socket.send(sendPacket);
                    }
                    System.out.println("Packet sent");
                }


            }




        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

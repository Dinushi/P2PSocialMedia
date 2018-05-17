package sample.CommunicationHandler;

import sample.EventHandler.ConversationHandler;
import sample.EventHandler.HeartBeatHandler;
import sample.EventHandler.NewPeerListner;
import sample.EventHandler.PostHandler;
import sample.Model.*;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReceivedPacketHandler extends Thread {
    Object receivedObject=null;
    InetAddress sender_ip;
    int sender_port;
    public  ReceivedPacketHandler(Object receivedObject, InetAddress sender_ip,int sender_port){
        System.out.println("sender_ip"+sender_ip);
        System.out.println("sender_port"+sender_port);
        this.receivedObject=receivedObject;
        this.sender_ip=sender_ip;
        this.sender_port=sender_port;
    }

    public void run(){//once a new packet is arrived it invoke a new packet Handler Thraed
        //select the type of the packet
        if (receivedObject instanceof Post) {
            System.out.println("Packet handler got post ");
            Post post = (Post) receivedObject;
            PostHandler.gotAPost(post);//notify the PostHandler to show the post in the Home
            //post.notifyController();

        }else if (receivedObject instanceof Reply) {
            System.out.println("Packet handler got reply ");
            Reply reply=(Reply) receivedObject;
            PostHandler.gotAReply(reply,sender_ip,sender_port);////notify the PostHandler about the reply
            //post.notifyController();

        }else if(receivedObject instanceof Message) {
            System.out.println("Packet handler got a Message object");
            Message msg = (Message) receivedObject;

            //once the Message is received send an ACK for the Sender
            ArrayList<ReceivingPeer> receiver=new ArrayList<>();
            receiver.add(new ReceivingPeer(sender_ip,sender_port));
            PeerConnection.getPeerConnection().sendViaSocket("MSGACK".concat(String.valueOf(msg.getUDPSeqNum())),receiver);

            ConversationHandler.gotAMessage(msg);

        }else if(receivedObject instanceof DiscoverdPeer) {
            System.out.println("Packet handler got Dis_peerrrrrrrrrrrrrr ###########################");
            DiscoverdPeer d_peer = (DiscoverdPeer) receivedObject;
            NewPeerListner.gotADiscoveredPeer(d_peer);

        }else if(receivedObject instanceof Peer){
            System.out.println("Packet handler got Peer");
            Peer peer = (Peer) receivedObject;
            NewPeerListner.gotAPeer(peer);

        }else if(receivedObject instanceof String){
            System.out.println("Packet handler got a string object");
            String str=(String)receivedObject;
            if(str=="SendMeSomePeers"){
                NewPeerListner.gotAPeerRequestForMorePeers(this.sender_ip,this.sender_port);
            }
            //A peer is checking my online presence
            else if(str=="AreYouON"){
                ArrayList<ReceivingPeer> receiver=new ArrayList<>();
                receiver.add(new ReceivingPeer(sender_ip,sender_port));
                PeerConnection.getPeerConnection().sendViaSocket("Yes",receiver);
                HeartBeatHandler.gotAPeerACK(this.sender_ip,this.sender_port);//Now also I know that this requested peer is online
            }
            //The peer I have checked for the online presence has Acknowledged
            else if(str=="Yes"){
                System.out.println("got to know a peer is online presence");
                HeartBeatHandler.gotAPeerACK(sender_ip,sender_port);
            }else if(str.startsWith("MSGACK")){
                System.out.println("Got a ACK for msg");
                String seq_num=str.substring(6);

                ArrayList<ReceivingPeer> msgreceiver=new ArrayList<>();
                msgreceiver.add(new ReceivingPeer(sender_ip,sender_port));

                MessageRetransmitter.getMessageRetransmitter().gotAAckForaMessage(seq_num,msgreceiver);

            }else if(str.startsWith("CONVACK")) {
                System.out.println("Got a ACK for conv");
                String seq_num = str.substring(7);

                ArrayList<ReceivingPeer> convreceiver = new ArrayList<>();
                convreceiver.add(new ReceivingPeer(sender_ip, sender_port));
                ConversationRetransmitter.getConversationRetransmitter().gotAAckForaConversation(seq_num, convreceiver);
            }

        }else if(receivedObject instanceof Conversation){
            System.out.println("Packet handler got a Conversation object");
            Conversation conv = (Conversation)receivedObject;
            ConversationHandler.gotAInitialConversation(conv);

            //once the Conversation is received send an ACK for the Sender
            ArrayList<ReceivingPeer> receiver=new ArrayList<>();
            receiver.add(new ReceivingPeer(sender_ip,sender_port));
            PeerConnection.getPeerConnection().sendViaSocket("CONVACK".concat(String.valueOf(conv.getUDPSeqNum())),receiver);

        } else if(receivedObject instanceof LocalDateTime){
            System.out.println("Packet handler got a LocaldateTime Object");
            LocalDateTime last_logged_time = (LocalDateTime) receivedObject;
            ArrayList<ReceivingPeer> receiver=new ArrayList<>();

            //send back a packet saying your online
            receiver.add(new ReceivingPeer(sender_ip,sender_port));
            PeerConnection.getPeerConnection().sendViaSocket("Yes",receiver);
            System.out.println("add the peer as online");

            //Notify the hearbeathandler about the online presence of the received peer
            HeartBeatHandler.gotPeerLoginAlert(sender_ip,sender_port,last_logged_time);//Now also I know that this requested peer
    }









    }
}

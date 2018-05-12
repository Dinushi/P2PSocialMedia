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

    public void run(){
        //select the type of the packet
        if (receivedObject instanceof Post) {
            System.out.println("Packet handler got post ");
            Post post = (Post) receivedObject;
            PostHandler.gotAPost(post);
            //post.notifyController();

        }else if (receivedObject instanceof Reply) {
            System.out.println("Packet handler got reply ");
            Reply reply=(Reply) receivedObject;
            PostHandler.gotAReply(reply,sender_ip,sender_port);
            //post.notifyController();

        }else if(receivedObject instanceof Message) {
            System.out.println("Packet handler got a Message object");
            Message msg = (Message) receivedObject;

            ArrayList<ReceivingPeer> receiver=new ArrayList<>();
            receiver.add(new ReceivingPeer(sender_ip,sender_port));
            PeerConnection.getPeerConnection().sendViaSocket("MSGACK".concat(String.valueOf(msg.getUDPSeqNum())),receiver);

            ConversationHandler.gotAMessage(msg);

        }else if(receivedObject instanceof DiscoverdPeer) {
            System.out.println("Packet handler got Dis_peer");
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
            /*
            else if(str=="AreYouON"){
                ArrayList<ReceivingPeer> receiver=new ArrayList<>();
                receiver.add(new ReceivingPeer(sender_ip,sender_port));
                PeerConnection.getPeerConnection().sendViaSocket("Yes",receiver);
                HeartBeatHandler.gotPeerACK(sender_ip,sender_port);//Now also I know that this requested peer is online
            }
            */
            else if(str=="Yes"){
                System.out.println("got to know a peer online presence");
                HeartBeatHandler.gotAPeerACK(sender_ip,sender_port);
            }else if(str.startsWith("MSGACK")){
                System.out.println("Got a ACK for msg");
                String seq_num=str.substring(6);

                ArrayList<ReceivingPeer> msgreceiver=new ArrayList<>();
                msgreceiver.add(new ReceivingPeer(sender_ip,sender_port));

                MessageRetransmitter.getMessageRetransmitter().gotAAckForaMessage(seq_num,msgreceiver);

            }else if(str.startsWith("CONVACK")) {
                System.out.println("Got a ACK for conv");
                String seq_num = str.substring(6);

                ArrayList<ReceivingPeer> convreceiver = new ArrayList<>();
                convreceiver.add(new ReceivingPeer(sender_ip, sender_port));
                ConversationRetransmitter.getConversationRetransmitter().gotAAckForaConversation(seq_num, convreceiver);
            }

        }else if(receivedObject instanceof Conversation){
            System.out.println("Packet handler got a Conversation object");
            Conversation conv = (Conversation)receivedObject;
            ConversationHandler.gotAInitialConversation(conv);

            //sending back the ACK for conversation
            ArrayList<ReceivingPeer> receiver=new ArrayList<>();
            receiver.add(new ReceivingPeer(sender_ip,sender_port));
            PeerConnection.getPeerConnection().sendViaSocket("CONVACK".concat(String.valueOf(conv.getUDPSeqNum())),receiver);

        } else if(receivedObject instanceof LocalDateTime){
            System.out.println("Packet handler got a LocaldateTime Object");
            LocalDateTime last_logged_time = (LocalDateTime) receivedObject;
            ArrayList<ReceivingPeer> receiver=new ArrayList<>();
            receiver.add(new ReceivingPeer(sender_ip,sender_port));
            PeerConnection.getPeerConnection().sendViaSocket("Yes",receiver);
            HeartBeatHandler.gotPeerLoginAlert(sender_ip,sender_port,last_logged_time);//Now also I know that this requested peer
    }



            //}else(req=""){

            //}
        //String type=receivedObject.getClass().getSimpleName();//this will not work
        //Post post = (Post) receivedObject;





    }
}

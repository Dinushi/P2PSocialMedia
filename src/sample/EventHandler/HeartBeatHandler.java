package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Controller.Validator;
import sample.DBHandler.DbHandler;
import sample.Model.Conversation;
import sample.Model.Message;
import sample.Model.Peer;
import sample.Model.Post;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HeartBeatHandler extends  Thread{

    public static ArrayList<Peer> allConnectedPeers;

    public void run(){
        if(!PeerConnection.initialLogin){
            DbHandler db=new DbHandler();
            allConnectedPeers=db.getAllPeers("T");
            db.closeConnection();
            ArrayList<ReceivingPeer> receivingPeers=new ArrayList<>();
            for (Peer sel_peer : allConnectedPeers) {
                receivingPeers.add(new ReceivingPeer(sel_peer.getIp(),sel_peer.getPort()));
            }
            System.out.println("Sending my last log out time"+Validator.last_logOuttime);
            PeerConnection.getPeerConnection().sendViaSocket(Validator.last_logOuttime,receivingPeers);
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            DbHandler db=new DbHandler();
            allConnectedPeers=db.getAllPeers("T");
            db.closeConnection();
        }

        while(true){
            try {
                ArrayList<ReceivingPeer> receivingPeers2=new ArrayList<>();
                int i=0;
                for (Peer sel_peer : allConnectedPeers) {
                    receivingPeers2.add(new ReceivingPeer(sel_peer.getIp(),sel_peer.getPort()));
                    allConnectedPeers.get(i).setOnlineStatus(false);
                    i++;
                }
                //This checks the online presence of user once five minutes
                PeerConnection.getPeerConnection().sendViaSocket("AreYouON",receivingPeers2);
                Thread.sleep(300000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
    //call when each user send a heartbeat to check online presence,known that the packet sender is also online
    public  static synchronized void gotAPeerACK(InetAddress ip, int port) {
        updateOnlineStatus(ip,port);

    }
    public static void updateOnlineStatus(InetAddress ip, int port){
        System.out.println("updating the online status of the peer");
        for (int i = 0; i < allConnectedPeers.size(); i++) {
            Peer peer = allConnectedPeers.get(i);
            if (peer.getIp() == ip && peer.getPort() == port) {
                allConnectedPeers.get(i).setOnlineStatus(true);
                return;
            }
        }
    }
    //call when a data about a user log in after a time
    public static void shareMissedDataWithPeer(InetAddress ip,int port, LocalDateTime last_logged_time){
        DbHandler db=new DbHandler();
        ReceivingPeer rec_peer=new ReceivingPeer(ip,port);
        ArrayList <ReceivingPeer> rec_peers=new ArrayList<>();
        rec_peers.add(rec_peer);

        //sending the missed posts
        ArrayList<Post> thePostsThisUserMissed=db.getNewlyCreatedPosts(last_logged_time);
        for(Post p: thePostsThisUserMissed){
            //each newly created missed post is sent to this peer when he reconnects
            PeerConnection.getPeerConnection().sendViaSocket(p,rec_peers);
        }

        //sending the missed new Conversations created
        String requestedPeerUsername=db.identifyThePeer(ip,port);
        ArrayList<Conversation> theConversationsThisUserMissed=db.getNewlyCreatedConversationsWithAssociatedMessages(requestedPeerUsername,last_logged_time,true);
        for(Conversation conv: theConversationsThisUserMissed){
            //each newly created missed post is sent to this peer when he reconnects
            PeerConnection.getPeerConnection().sendViaSocket(conv,rec_peers);
        }

        //sending the missed messages of conversations that the requested peer already know.has created by someUser before his log out
        ArrayList<Conversation> theOldConAssociatedWithThisPeer=db.getNewlyCreatedConversationsWithAssociatedMessages(requestedPeerUsername,last_logged_time,true);
        for(Conversation conv: theOldConAssociatedWithThisPeer){
            ArrayList<Message> newMessagesICreatedForConv=db.getMessages(conv.getConversation_id(),conv.getConversation_initiator().getUsername(),last_logged_time,true);
            for(Message msg: newMessagesICreatedForConv){
                PeerConnection.getPeerConnection().sendViaSocket(msg,rec_peers);
            }
        }

        db.closeConnection();
    }
    public  static synchronized void gotPeerLoginAlert(InetAddress ip, int port, LocalDateTime last_logged_time){
        //what happens when i get a packet with last connected time of a peer
        //Know that a peer is online and requesting data.
        System.out.println("Got a peer Login alert");
        updateOnlineStatus(ip,port);
        shareMissedDataWithPeer(ip,port,last_logged_time);
    }

}

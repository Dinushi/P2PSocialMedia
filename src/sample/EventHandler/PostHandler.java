package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Controller.Validator;
import sample.DBHandler.DbHandler;
import sample.Model.Peer;
import sample.Model.Post;
import sample.Model.Reply;

import java.net.InetAddress;
import java.util.ArrayList;

public class PostHandler {
    static ArrayList<ReceivingPeer> peerIP_ports;

    //If you want to re transmit several times.Use a thread here to sleep and run.
    public static void sentThePostToPeers(Post post){
        DbHandler db=new DbHandler();
        db.addNewPost(post);
        peerIP_ports=db.selectAllPeerAddresses("T");
        db.closeConnection();
        PeerConnection.getPeerConnection().sendViaSocket(post,peerIP_ports);
        //post is sent to all these peers
    }
    public synchronized static  void gotAPost(Post post){
        DbHandler db=new DbHandler();
        db.addNewPost(post);
        post.notifyController();
    }
    public synchronized static void addToDb(Post post){
        DbHandler db=new DbHandler();
        db.addNewPost(post);
        db.closeConnection();

    }
    public synchronized static void sendReplyToPeersIntersted(Reply reply){
        //this method send the reply to all my peers,because the reply is for a my post
        DbHandler db=new DbHandler();
        peerIP_ports=db.selectAllPeerAddresses("T");
        db.closeConnection();
        PeerConnection.getPeerConnection().sendViaSocket(reply,peerIP_ports);

    }
    public synchronized static void sendTheReplyToThePostOwner(Reply reply,String postCreator){
        //send the reply to post owner
        DbHandler db=new DbHandler();
        Peer p=db.getPeer(postCreator);
        ArrayList<ReceivingPeer> peerIpPort=new ArrayList<>();
        peerIpPort.add(new ReceivingPeer(p.getIp(),p.getPort()));
        db.closeConnection();
        PeerConnection.getPeerConnection().sendViaSocket(reply,peerIpPort);
    }
    public synchronized static  void gotAReply(Reply reply, InetAddress reply_sender_ip,int reply_sender_port){
        DbHandler db=new DbHandler();
        Post post=db.checkTheAvailabilityOfAPost(reply.getUsername(),reply.getPost_id());
        if(post==null){
            System.out.println("The reply received does not belong to me");
        }else{
            db.addNewReply(reply);
            //if the reply is for a my post.Send it to other peers.
            if(post.getUsername().equalsIgnoreCase(Validator.username)){
                DbHandler db2=new DbHandler();
                peerIP_ports=db2.selectAllPeerAddresses("T");
                db2.closeConnection();

                //need to remove the reply sender from the list
                ArrayList<ReceivingPeer> edited_peerIP_ports=new ArrayList<>();
                for(ReceivingPeer peer:peerIP_ports){
                    if(peer.getIP()!=reply_sender_ip || peer.getPort()!=reply_sender_port){
                        edited_peerIP_ports.add(peer);
                    }
                }
                PeerConnection.getPeerConnection().sendViaSocket(reply,edited_peerIP_ports);
            }

        }




    }
}

package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.DBHandler.DbHandler;
import sample.Model.Post;
import sample.Model.Reply;

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

    }
    public synchronized static  void gotAReply(Reply reply){


    }
}

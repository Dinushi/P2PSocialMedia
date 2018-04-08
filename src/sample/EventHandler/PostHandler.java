package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.DBHandler.DbHandler;
import sample.Model.Post;

import java.util.ArrayList;

public class PostHandler {
    static ArrayList<ReceivingPeer> peerIP_ports;

    //If you want to re transmit several times.Use a thread here to sleep and run.
    public static void sentThePostToPeers(Post post){
        DbHandler db=new DbHandler();
        peerIP_ports=db.selectAllPeerAddresses("T");
        db.closeConnection();
        PeerConnection.getPeerConnection().sendViaSocket(post,peerIP_ports);
        //post is sent to all these peers
    }
}

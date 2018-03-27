package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.DBHandler.DatabaseHandler;
import sample.DBHandler.DbHandler;
import sample.Model.DiscoverdPeer;
import sample.Model.Owner;
import sample.Model.Peer;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class NewPeerListner {
    static ArrayList<DiscoverdPeer> discoverdPeers=new ArrayList<DiscoverdPeer>();

    //when ever a new peer is discoverd this method is called
    public static void update(DiscoverdPeer peer){
        discoverdPeers.add(peer);
        //new DatabaseHandler().updatePeerDetails(peer);
        //add all these peers to the database
    }
//use some thrad mechanism to sent re transmissions
    public static void sendJoinRequestToDiscoverdPeer(){
        DbHandler db=new DbHandler();
        if(discoverdPeers.isEmpty()){
            discoverdPeers=db.getAllReceivedPeers();
        }
        PeerConnection peerConn = new PeerConnection();
        ArrayList<ReceivingPeer> receivers = new ArrayList<>();
        for (DiscoverdPeer peer : discoverdPeers) {
            ReceivingPeer receiver = new ReceivingPeer(peer.getIp(), peer.getPort());
            receivers.add(receiver);
            db.addNewReceivedPeer(peer);
        }
        peerConn.sendViaSocket(new DiscoverdPeer("Join",Owner.myUsername,Owner.myIP,Owner.myPort), receivers);
        //add these received peers to the database

        db.closeConnection();

    }
    public synchronized static void gotAPeerConfirmation(Peer peer){
        DiscoverdPeer newPeer=new DiscoverdPeer("Join",peer.getUsername(),peer.getIp(),peer.getPort());
        discoverdPeers.remove(newPeer);//check for the correct performance of hashcode equals override
        //update the database
        DbHandler db=new DbHandler();
        db.addAnewPeer(peer);
        db.removeAdiscoverdPeer(newPeer);
        db.closeConnection();
        //show notification to the user
    }

    public static void gotAPeerJoinRequest(DiscoverdPeer newPeer){
        System.out.println(newPeer.getUsername());
        //show these requests to the user and get his reply.
        //according to user reply sent acceptance message
        //sent the username string as the reply

    }
    public static  void sendTheConfirmation(){


    }
}

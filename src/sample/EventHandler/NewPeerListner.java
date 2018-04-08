package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Controller.Validator;
import sample.DBHandler.DbHandler;
import sample.Model.DiscoverdPeer;
import sample.Model.Owner;
import sample.Model.Peer;

import java.util.ArrayList;

public class NewPeerListner {
    private static ArrayList<DiscoverdPeer> peersSentByBS=new ArrayList<DiscoverdPeer>();//hold the requests sent by the Bs
    private static ArrayList<Peer> peerRequests=new ArrayList<Peer>();//hold the peers sent by the peers

    //when ever a new peer is discoverd this method is called
    public static void update_PeersSentByBS(DiscoverdPeer peer){
        peersSentByBS.add(peer);
        //add all these peers to the database
    }

    //use some thrad mechanism to sent re transmissions
    public static void sendJoinRequestToDiscoverdPeersFromBs(){
        System.out.println("Came to send requests to discoverd peers from BS");
        DbHandler db=new DbHandler();
        PeerConnection peerConn = PeerConnection.getPeerConnection();
        ArrayList<ReceivingPeer> receivers = new ArrayList<>();

        for (DiscoverdPeer d_peer : peersSentByBS) {
            System.out.println("A requesting Peer"+d_peer.getPort()+" "+d_peer.getIp());
            ReceivingPeer receiver = new ReceivingPeer(d_peer.getIp(), d_peer.getPort());
            receivers.add(receiver);
            db.addNewDiscoverdPeer(d_peer,"T");

        }
        if(!receivers.isEmpty()) {
            peerConn.sendViaSocket(Validator.thisPeer, receivers);
        }
        //add these received peers to the database
        db.closeConnection();
    }
    public static void requestPeerToSharePeerDetails(Peer peer){

    }


    //when a peer has confirmed.
    public synchronized static void gotAPeer(Peer newPeer){
        System.out.println("Printing details of received new Peer  "+newPeer.getUsername()+" "+newPeer.getIp()+" "+newPeer.getPort()+" "+newPeer.getHometown());

        DbHandler db=new DbHandler();
        boolean result=db.removeAdiscoverdPeer(newPeer.getUsername());
        System.out.println("reslt"+result);

        if(result){
            newPeer.setJoined(true);
            db.addAnewPeer(newPeer);

            //connected with this user
        }else{
            db.addAnewPeer(newPeer);
            //setJoined is false.Neeed to get user confirmation and send back this peer
        }
        //peerRequests.remove(newPeer);//check for the correct performance of hashcode equals override
        db.closeConnection();
        //show notification to the user if possible
    }


    //send reply to received requests
    public static  void sendTheConfirmation(ArrayList <Peer> selectedPeers,ArrayList<Peer> rejectedPeer){
        DbHandler db=new DbHandler();
        for (Peer rej_peer : rejectedPeer) {
           peerRequests.remove(rej_peer);
           db.removeAPeer(rej_peer.getUsername());
        }
        ArrayList<ReceivingPeer> receivingPeers=new ArrayList<>();
        for (Peer sel_peer : selectedPeers) {
            db.updatePeerConfirmation(sel_peer.getUsername());//add the confirmed peer to the database also
            receivingPeers.add(new ReceivingPeer(sel_peer.getIp(),sel_peer.getPort()));
        }


        PeerConnection.getPeerConnection().sendViaSocket(Validator.thisPeer,receivingPeers);
    }

    public static ArrayList<Peer> getRequestedPeers() {
        DbHandler db = new DbHandler();
        peerRequests = db.getAllPeers("F");
        db.closeConnection();
        peerRequests.remove(Validator.thisPeer);
        return peerRequests;

    }

}

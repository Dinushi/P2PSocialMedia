package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Controller.Validator;
import sample.DBHandler.DbHandler;
import sample.Model.DiscoverdPeer;
import sample.Model.Owner;
import sample.Model.Peer;

import java.net.InetAddress;
import java.util.ArrayList;

public class NewPeerListner {
    private static ArrayList<DiscoverdPeer> peersSentByBS=new ArrayList<DiscoverdPeer>();//hold the requests sent by the Bs
    private static ArrayList<Peer> peerRequests=new ArrayList<Peer>();//hold the peers sent by the peers

    //when ever a new peer is discoverd this method is called
    public static void update_PeersSentByBS(DiscoverdPeer d_peer){
        peersSentByBS.add(d_peer);
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
            boolean result=db.addNewDiscoverdPeer(d_peer,"T");
            if(result){
                System.out.println("The known peer has stored");
            }

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
        int result = db.removeAdiscoverdPeer(newPeer.getUsername());
        System.out.println("reslt"+result);

        if(result==1){
            newPeer.setJoined(true);
            db.addAnewPeer(newPeer);

            //connected with this user
        }else if(result==0){
            Peer peer_test=db.getPeer(newPeer.getUsername());
            if(peer_test==null){
                db.addAnewPeer(newPeer);
            }else{
                System.out.println("Got changes for profile data of a peer you know");
                db.updatePeerDataChanges(newPeer);
            }

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
            boolean result=db.updatePeerConfirmation(sel_peer.getUsername());//add the confirmed peer to the database also
            if(result){
                System.out.println("Successfully updated the perr requested as a friend of you");
            }
            receivingPeers.add(new ReceivingPeer(sel_peer.getIp(),sel_peer.getPort()));
        }


        PeerConnection.getPeerConnection().sendViaSocket(Validator.thisPeer,receivingPeers);
    }

    public static ArrayList<Peer> getRequestedPeers() {
        DbHandler db = new DbHandler();
        peerRequests = db.getAllPeers("F");
        db.closeConnection();
        boolean result=peerRequests.remove(Validator.thisPeer);
        if(!result){
            System.out.println("Removing this peer from result set has failed");
        }

        return peerRequests;

    }
    public static void sendRequestForMorePeerDetails(ArrayList<Peer> selectedPeers){
        ArrayList<ReceivingPeer> receivingPeers=new ArrayList<>();
        for (Peer sel_peer : selectedPeers) {
            receivingPeers.add(new ReceivingPeer(sel_peer.getIp(),sel_peer.getPort()));
            PeerConnection peerConn = PeerConnection.getPeerConnection();
            peerConn.sendViaSocket("SendMeSomePeers",receivingPeers);
        }

    }
    public static void gotAPeerRequestForMorePeers(InetAddress senderIp,int senderPort){
        DbHandler db=new DbHandler();
        ArrayList<ReceivingPeer> receivingPeers=new ArrayList<>();
        receivingPeers.add(new ReceivingPeer(senderIp,senderPort));
        ArrayList<Peer> allPeers=db.getAllPeers("T");
        db.closeConnection();
        PeerConnection peerConn = PeerConnection.getPeerConnection();
        if(allPeers.size()<4){
            for(int i=0;i<allPeers.size();i++){
                DiscoverdPeer d_peer=new DiscoverdPeer("PeerInfo",allPeers.get(i).getUsername(),allPeers.get(i).getIp(),allPeers.get(i).getPort());
                peerConn.sendViaSocket(d_peer,receivingPeers);
            }
        } else {
            for(int i=0;i<4;i++){
                DiscoverdPeer d_peer=new DiscoverdPeer("PeerInfo",allPeers.get(i).getUsername(),allPeers.get(i).getIp(),allPeers.get(i).getPort());
                peerConn.sendViaSocket(d_peer,receivingPeers);
            }
        }


    }
    public static void gotADiscoveredPeer(DiscoverdPeer d_peer){
        DbHandler db=new DbHandler();
        boolean result=db.addNewDiscoverdPeer(d_peer,"F");
        if(result){
            System.out.println("The recived peer detail has stored");
        }
    }
    public static void sendPeerProfileChanges(){
        System.out.println("sending the changes in the peer profile");
        DbHandler db=new DbHandler();
        ArrayList<ReceivingPeer> peerIP_ports=db.selectAllPeerAddresses("T");

        PeerConnection peerConn = PeerConnection.getPeerConnection();
        if(!peerIP_ports.isEmpty()) {
            peerConn.sendViaSocket(Validator.thisPeer, peerIP_ports);
        }
        //add these received peers to the database
        db.closeConnection();
    }
    public static void sendJoinRequestToDiscoverdPeers(ArrayList<DiscoverdPeer> allDiscoveredPeers){
        System.out.println("Came to send requests to discoverd peers from known peers");
        DbHandler db=new DbHandler();
        PeerConnection peerConn = PeerConnection.getPeerConnection();
        ArrayList<ReceivingPeer> receivers = new ArrayList<>();

        for (DiscoverdPeer d_peer : allDiscoveredPeers) {
            System.out.println("A discoverd Peer"+d_peer.getPort()+" "+d_peer.getIp());
            ReceivingPeer receiver = new ReceivingPeer(d_peer.getIp(), d_peer.getPort());
            receivers.add(receiver);
            boolean result=db.updateDiscoverdPeer(d_peer.getUsername());
            if(result){
                System.out.println("Send a join request to Discovered peer");
            }
        }
        if(!receivers.isEmpty()) {
            peerConn.sendViaSocket(Validator.thisPeer, receivers);
        }
        //add these received peers to the database
        db.closeConnection();
    }

}

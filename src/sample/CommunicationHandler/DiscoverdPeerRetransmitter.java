package sample.CommunicationHandler;

import sample.Controller.Validator;
import sample.Model.DiscoverdPeer;
import sample.Model.Message;

import java.util.ArrayList;

public class DiscoverdPeerRetransmitter {
    private static  DiscoverdPeerRetransmitter discoverdPeerRetransmitter=null;
    private ArrayList<DiscoverdPeer> allRequestedDiscoverdPeers;




    private DiscoverdPeerRetransmitter(){
        this.allRequestedDiscoverdPeers= new ArrayList<>();
    }

    public static DiscoverdPeerRetransmitter getDiscoverdPeerRetransmitter(){
        if(discoverdPeerRetransmitter==null) {
            discoverdPeerRetransmitter = new DiscoverdPeerRetransmitter();
            return discoverdPeerRetransmitter;
        }else{
            return discoverdPeerRetransmitter;
        }
    }

    //keep the known peer until it is acknowledged
    public ArrayList addARequestedPeer(DiscoverdPeer d_peer){
        allRequestedDiscoverdPeers.add(d_peer);
        return allRequestedDiscoverdPeers;
    }

    public Message gotAAckForaMessage(ArrayList<ReceivingPeer> theReceiverWhoAcknowledged){
        for(DiscoverdPeer r_peer:this.allRequestedDiscoverdPeers){
            if(r_peer.getIp()==theReceiverWhoAcknowledged.get(0).getIP() && r_peer.getPort()==theReceiverWhoAcknowledged.get(0).getPort()){
                allRequestedDiscoverdPeers.remove(r_peer);
                    //test the above for proper performance
                break;
            }
        }
        return null;
    }

    public void run(){
        while(true){
            try {
                System.out.println("started Retransmitter thread");
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("going to resend a Discoverd Peeer from Bs");
            int i=0;
            for (DiscoverdPeer d_peer : allRequestedDiscoverdPeers){
                    System.out.println("ahh no ack got for this msg");
                    ArrayList<ReceivingPeer> receivers = new ArrayList<>();
                    ReceivingPeer receiver = new ReceivingPeer(d_peer.getIp(), d_peer.getPort());
                    receivers.add(receiver);
                    receivers.add(receiver);
                    PeerConnection.getPeerConnection().sendViaSocket(Validator.thisPeer,receivers);
                    //old entries are removed
                    this.allRequestedDiscoverdPeers.remove(i);
                    i++;

                }

        }

    }
}



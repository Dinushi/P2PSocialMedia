package sample.EventHandler;

import sample.DBHandler.DatabaseHandler;
import sample.Model.DiscoverdPeer;

import java.util.ArrayList;

public class NewPeerListner {
    static ArrayList<DiscoverdPeer> discoverdPeers=new ArrayList<DiscoverdPeer>();

    //when ever a new peer is discoverd this method is called
    public static void update(DiscoverdPeer peer){
        discoverdPeers.add(peer);
        //new DatabaseHandler().updatePeerDetails(peer);
        //add all these peers to the database
    }

    public static void sendJoinRequestToDiscoverdPeer(DiscoverdPeer peer){



    }

}

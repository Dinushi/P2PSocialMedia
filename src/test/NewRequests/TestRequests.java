package test.NewRequests;

import sample.EventHandler.NewPeerListner;
import sample.Model.Peer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class TestRequests {
    public void addANewPeerRequest() throws UnknownHostException {
        Peer p1=new Peer("DinushiRanagalage", InetAddress.getLocalHost(),1234,false);

        Peer p2=new Peer("Pasindu Chinthiya", InetAddress.getLocalHost(),3456,false);
        Peer p3=new Peer("Thenumi THEmi", InetAddress.getLocalHost(),3457,false);
        //NewPeerListner.gotAPeerJoinRequest(p1);
        //NewPeerListner.gotAPeerJoinRequest(p2);
        //NewPeerListner.gotAPeerJoinRequest(p3);
    }
}

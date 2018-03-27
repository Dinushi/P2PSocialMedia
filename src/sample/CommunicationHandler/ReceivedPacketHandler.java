package sample.CommunicationHandler;

import sample.DBHandler.DbHandler;
import sample.EventHandler.NewPeerListner;
import sample.Model.DiscoverdPeer;
import sample.Model.Message;
import sample.Model.Peer;
import sample.Model.Post;
import sun.rmi.runtime.NewThreadAction;

import java.io.ObjectInputStream;
import java.net.InetAddress;

public class ReceivedPacketHandler extends Thread {
    Object receivedObject=null;
    InetAddress sender_ip;
    int sender_port;
    public  ReceivedPacketHandler(Object receivedObject, InetAddress sender_ip,int sender_port){
        this.receivedObject=receivedObject;
        this.sender_ip=sender_ip;
        this.sender_port=sender_port;
    }

    public void run(){
        //select the type of the packet
        if (receivedObject instanceof Post) {
            Post post=(Post)receivedObject;
            post.notifyController();
        }else if(receivedObject instanceof Message){
            Message msg=(Message)receivedObject;
        }else if(receivedObject instanceof DiscoverdPeer) {
            DiscoverdPeer Req = (DiscoverdPeer) receivedObject;
            NewPeerListner.gotAPeerJoinRequest(Req);
        }else if(receivedObject instanceof Peer){
            Peer peer = (Peer) receivedObject;
            NewPeerListner.gotAPeerConfirmation(peer);
        }


            //}else(req=""){

            //}
        //String type=receivedObject.getClass().getSimpleName();//this will not work
        //Post post = (Post) receivedObject;





    }
}

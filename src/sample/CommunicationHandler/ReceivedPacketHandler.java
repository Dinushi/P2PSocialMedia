package sample.CommunicationHandler;

import sample.EventHandler.NewPeerListner;
import sample.Model.DiscoverdPeer;
import sample.Model.Message;
import sample.Model.Peer;
import sample.Model.Post;

import java.net.InetAddress;

public class ReceivedPacketHandler extends Thread {
    Object receivedObject=null;
    InetAddress sender_ip;
    int sender_port;
    public  ReceivedPacketHandler(Object receivedObject, InetAddress sender_ip,int sender_port){
        System.out.println("sender_ip"+sender_ip);
        System.out.println("sender_port"+sender_port);
        this.receivedObject=receivedObject;
        this.sender_ip=sender_ip;
        this.sender_port=sender_port;
    }

    public void run(){
        //select the type of the packet
        if (receivedObject instanceof Post) {
            System.out.println("Packet handler got post ");
            Post post=(Post)receivedObject;
            post.notifyController();
        }else if(receivedObject instanceof Message){
            System.out.println("Packet handler got Meaasge");
            Message msg=(Message)receivedObject;
        }else if(receivedObject instanceof DiscoverdPeer) {
            System.out.println("Packet handler got Dis_peer");
            DiscoverdPeer Req = (DiscoverdPeer) receivedObject;
            //NewPeerListner.gotAPeerJoinRequest(Req);
        }else if(receivedObject instanceof Peer){
            System.out.println("Packet handler got Peer");
            Peer peer = (Peer) receivedObject;
            NewPeerListner.gotAPeer(peer);
        }else if(receivedObject instanceof String){
            System.out.println("Packet handler got a string object");
            System.out.println(((String)receivedObject));

    }


            //}else(req=""){

            //}
        //String type=receivedObject.getClass().getSimpleName();//this will not work
        //Post post = (Post) receivedObject;





    }
}

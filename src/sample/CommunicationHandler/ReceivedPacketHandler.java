package sample.CommunicationHandler;

import sample.Model.Post;

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
        String type=receivedObject.getClass().getSimpleName();//this will not work
        Post post = (Post) receivedObject;
        System.out.println("Receiver:"+post.getContent());
        System.out.println("Receiver:"+post.getDate_created());
        if(type=="Post"){

        }


    }
}

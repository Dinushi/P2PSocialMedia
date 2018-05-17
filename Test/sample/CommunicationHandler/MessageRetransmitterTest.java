package sample.CommunicationHandler;

import org.junit.Before;
import org.junit.Test;
import sample.Model.Message;

import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

public class MessageRetransmitterTest {
    MessageRetransmitter msg_retrans;
    Message msg;
    ArrayList<ReceivingPeer> msgreceiver;


    @Before
    public void readyForTest() throws  java.net.UnknownHostException {
        this.msg_retrans=MessageRetransmitter.getMessageRetransmitter();
        this.msg=new Message("Boo","I will help you");
        msg.setUDPSeqNum("0");
        msgreceiver=new ArrayList<>();
        msgreceiver.add(new ReceivingPeer(InetAddress.getByName("127.10.1.0"),1234));
        msg_retrans.addAMessage(msg,msgreceiver);

    }
    @Test
    public void getMessageRetransmitter_test(){
        MessageRetransmitter value=MessageRetransmitter.getMessageRetransmitter();
        assertEquals(msg_retrans,value);

    }
    @Test
    public void addAMessage() throws java.net.UnknownHostException {
        Message msg=new Message("Boo","I am not the fake one");
        msg.setUDPSeqNum("1");
        ArrayList<ReceivingPeer> rec=new ArrayList<>();
        ReceivingPeer r1=new ReceivingPeer(InetAddress.getByName("localhost"),1234);
        rec.add(r1);
        Map<String,ArrayList<ReceivingPeer>> receiversOfEachPacket=msg_retrans.addAMessage(msg,rec);
        ReceivingPeer p1=receiversOfEachPacket.get("1").get(0);
        assertEquals(InetAddress.getByName("localhost"),p1.getIP());
    }
    @Test
    public void gotAACK_test(){
        Message msg_deleted = msg_retrans.gotAAckForaMessage("0",msgreceiver);
        assertEquals(msg_deleted,msg);

    }
    @Test
    public void gotALateACK_test(){
        Message msg_deleted = msg_retrans.gotAAckForaMessage("1",msgreceiver);
        //assertEquals(msg_deleted,null);

    }



}
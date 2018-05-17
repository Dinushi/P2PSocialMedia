package sample.CommunicationHandler;

import org.junit.Before;
import org.junit.Test;
import sample.Model.Conversation;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

public class ConversationRetransmitterTest {
    ConversationRetransmitter conv_retrans;
    Conversation conv;
    ArrayList<ReceivingPeer> msgreceiver;


    @Before
    public void readyForTest() throws  java.net.UnknownHostException {
        this.conv_retrans=ConversationRetransmitter.getConversationRetransmitter();
        this.conv=new Conversation();
        conv.setUDPSeqNum("0");
        msgreceiver=new ArrayList<>();
        msgreceiver.add(new ReceivingPeer(InetAddress.getByName("127.10.1.0"),1234));
        conv_retrans.addAConversation(conv,msgreceiver);

    }
    @Test
    public void getMessageRetransmitter_test(){
        ConversationRetransmitter value=ConversationRetransmitter.getConversationRetransmitter();
        assertEquals(conv_retrans,value);

    }
    @Test
    public void addAMessage() throws java.net.UnknownHostException {
        Conversation conv=new Conversation();
        conv.setUDPSeqNum("1");
        ArrayList<ReceivingPeer> rec=new ArrayList<>();
        ReceivingPeer r1=new ReceivingPeer(InetAddress.getByName("localhost"),1234);
        rec.add(r1);
        Map<String,ArrayList<ReceivingPeer>> receiversOfEachPacket=conv_retrans.addAConversation(conv,rec);
        ReceivingPeer p1=receiversOfEachPacket.get("1").get(0);
        assertEquals(InetAddress.getByName("localhost"),p1.getIP());
    }
    @Test
    public void gotAACK_test(){
        Conversation con_deleted = conv_retrans.gotAAckForaConversation("0",msgreceiver);
        assertEquals(con_deleted,conv);

    }
    @Test
    public void gotALateACK_test(){
        Conversation msg_deleted = conv_retrans.gotAAckForaConversation("1",msgreceiver);
        //assertEquals(msg_deleted,null);

    }


}
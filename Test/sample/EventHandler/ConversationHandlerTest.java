package sample.EventHandler;

import org.junit.Before;
import org.junit.Test;
import sample.Model.Message;
import sample.Model.Peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class ConversationHandlerTest {



    @Test
    public void adANewInitialConversation(){

    }

    @Test
    public void addAMsgToDb() throws UnknownHostException {
        Message msg=new Message("Boo","I am not the best person");
        msg.setConversation_id(1);
        msg.setMessage_id(1);
        msg.setConversation_initiator(new Peer("Boo", InetAddress.getByName("127.10.2.3"),1234,true));
        boolean result=ConversationHandler.addMsgToDb(msg);
        assertEquals(true,result);

    }

}
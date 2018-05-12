package sample.DBHandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.junit.Before;
import org.junit.Test;
import sample.Controller.Validator;
import sample.Model.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DbHandlerTest {
    DbHandler db;
    Peer me;
    Peer p1;

    @Before
    public void init(){
        db=new DbHandler();
    }

    @Test
    public void closeConnection() {
    }
    @Test
    public void addMe() throws UnknownHostException {
        me=new Peer("Boo",Validator.thisPeer.getIp(),Validator.thisPeer.getPort(),true);
        boolean result=db.addAnewPeer(me);
        assertEquals(true,result);

    }

    @Test
    public void addAnewPeer() throws UnknownHostException {
        p1=new Peer("Chinthiya", InetAddress.getByName("192.10.3.4"),4566,false);
        boolean result=db.addAnewPeer(p1);
        assertEquals(true,result);

    }


    @Test
    public void addNewConversation() {
        Conversation conv=new Conversation();
        conv.setConversation_id(db.getMaxConvID()+1);
        conv.setConversation_initiator(Validator.thisPeer);
        conv.setUnseenMessages(false);
        conv.addPartner(p1);
        conv.setTitle(p1.getUsername());
        boolean result=db.addNewConv(conv);
        assertEquals(true,result);


    }

    @Test
    public void getPeer() {
        Peer p2=db.getPeer("Boo");
        assertEquals(Validator.thisPeer,p2);
    }

    @Test
    public void getAPeerNotInTable() {
        Peer p3=db.getPeer("Chinthi");
        assertEquals(null,p3);
    }

    @Test
    public void updatePeerConfirmation() {
        boolean result=db.updatePeerConfirmation("Chinthiya");
        assertEquals(true,result);

    }

    @Test
    public void removeAPeer() throws UnknownHostException {

        Peer p=new Peer("Nandani", InetAddress.getByName("192.10.3.6"),4366,true);
        db.addAnewPeer(p);
        db.removeAPeer("Boo");
        Peer p_out=db.getPeer("Boo");
        assertNull(p_out);
    }

    @Test
    public void getAllPeers() {
        ArrayList<Peer> allpeers=db.getAllPeers("T");
        assertNotNull(allpeers);
    }

    @Test
    public void identifyThePeer() throws UnknownHostException {
        String username=db.identifyThePeer(InetAddress.getByName("192.10.3.4"),4566);
        assertEquals("Chinthiya",username);
    }

    @Test
    public void selectAllPeerUsernames() {
        ArrayList<String> peerUsernmaes=db.selectAllPeerUsernames();
    }

    @Test
    public void getNewlyCreatedPosts() {
    }

    @Test
    public void getPosts() {

    }

    @Test
    public void checkTheAvailabilityOfAPost() {
        Post post=db.checkTheAvailabilityOfAPost("Boo",1);
        assertNull(post);
    }
    Post p;
    @Test
    public void addNewPost() {
        p=new Post("Boo","Hii all",1);
        boolean res =db.addNewPost(p);
        assertEquals(true,res);
    }

    @Test
    public void getMyMaxPostID() {
        int post_id=db.getMyMaxPostID();
        assertNotNull(post_id);
    }

    @Test
    public void selectAllPeerAddresses() {
    }

    @Test
    public void addNewDiscoverdPeer() throws UnknownHostException {
        DiscoverdPeer peer=new DiscoverdPeer("Join","Pasi",InetAddress.getByName("234.56.8.9"),4567);
        boolean result=db.addNewDiscoverdPeer(peer,"F");
        assertEquals(true,result);
    }

    @Test
    public void updateDiscoverdPeer() {
        boolean res=db.updateDiscoverdPeer("Pasi");
        assertEquals(true,res);
        boolean res2=db.updateDiscoverdPeer("Pasiii");
        assertEquals(false,res2);

    }

    @Test
    public void selectAllDiscoveredPeers() {

    }

    @Test
    public void removeAdiscoverdPeer() {
        int val=db.removeAdiscoverdPeer("Pasi");
        assertEquals(val,1);
    }

    @Test
    public void addNewReply() {
        Reply reply=new Reply("Boo","pasinduuu",1,1);
        boolean re=db.addNewReply(reply);
        assertEquals(true,re);
    }

    @Test
    public void getMyMaxReplyID() {
        int reply_id=db.getMyMaxReplyID(p.getPostID());
        assertNotNull(reply_id);
    }

    @Test
    public void getReplies() {
        ArrayList<Reply> replirs=db.getReplies(p.getPostID());
        assertNotNull(replirs);
    }


    Conversation conv;
    @Test
    public void addNewConv() {
        conv.setConversation_id(4);
        conv.setConversation_initiator(Validator.thisPeer);
        conv.setUnseenMessages(false);
        conv.addPartner(p1);
        conv.setTitle(p1.getUsername());
        boolean result=db.addNewConv(conv);
        assertEquals(true,result);
    }


    @Test
    public void getAConversation() {
        Conversation conv2=db.getAConversation(4,"Boo");
        assertEquals(conv2,conv);

    }

    @Test
    public void addAMessage() {
        Message msg=new Message("Boo","Hiii chin");
        conv.addMessage(msg);
        boolean result=db.addAMessage(msg);
        assertEquals(result,true);

        Message msg2=new Message("Booo","Hiii chin");
        boolean result2=db.addAMessage(msg);
        assertEquals(result2,false);
    }
    @Test
    public void getMaxConvID() {
        int id=db.getMaxConvID();
        assertNotNull(id);
    }

    @Test
    public void getMaxMsgId() {
        int id=db.getMaxMsgId(conv);
        assertNotNull(id);
    }
    @Test
    public void addChatPartnersToChatTable() {
        boolean tres=db.addChatPartnersToChatTable(2,"Chinthiya","Chin");
        assertEquals(false,tres);
        db.addChatPartnersToChatTable(4,"Boo","Chin");

    }

    @Test
    public void getChatPartners() {
        ArrayList<Peer> chatPatners=db.getChatPartners(4,"Boo");
        assertNotNull(chatPatners);
    }

    @Test
    public void removeAConversation() {
        int re=db.removeAConversation(conv);
        assertEquals(1,re);
    }
    @Test
    public void checkExistanceOfAChatRecord() {
        boolean res=db.checkExistanceOfAChatRecord(1,"Boo");
        assertEquals(res,false);

    }

    @Test
    public void updatePeerDataChanges() throws UnknownHostException {
        Peer p1=new Peer("Chinthiya", InetAddress.getByName("192.10.3.4"),4566,true);
        p1.setStatus("Hiii my beloveds");
        db.updatePeerDataChanges(p1);
        Peer p2=db.getPeer("Chinthiya");
        assertEquals(p2.getStatus(),"Hiii my beloveds");

    }
}
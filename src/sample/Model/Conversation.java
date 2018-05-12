package sample.Model;

import sample.CommunicationHandler.HeartbeatAgent;
import sample.DBHandler.DbHandler;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Conversation implements Serializable {
    private ArrayList<Peer> partners;
    private ArrayList<Message> messages;
    private  LocalDateTime started_date;
    private Peer conversation_initiator;
    private int conversation_id;
    private boolean unseenMessages;
    private String title;
    private String UDPSeqNum;
    private long sentTimeOfConversationinMillis;


    public Conversation(){
        partners=new ArrayList<>();
        messages=new ArrayList<>();
        this.setStarted_date(LocalDateTime.now());

    }

    public void selectPeer(String partnerUsername){
        //connect to db and get partner data
        DbHandler db=new DbHandler();
        Peer peer=db.getPeer(partnerUsername);
        new HeartbeatAgent(peer.getIp(),peer.getPort()).start();
        //find a way to get back the reply and set the online status
        this.addPartner(peer);
        db.closeConnection();

    }
    public void addPartner(Peer p){
        this.partners.add(p);

    }

    public void addMessage(Message msg){
        this.messages.add(msg);

    }
    public void removePartner(int index){
        this.messages.remove(index);

    }


    public static ArrayList<String> selectAllPeerUsernames(){
        DbHandler db=new DbHandler();
        ArrayList<String> allpeers=db.selectAllPeerUsernames();
        db.closeConnection();
        return allpeers ;
    }

    public LocalDateTime getStarted_date() {
        return started_date;
    }

    public void setStarted_date(LocalDateTime started_date) {
        this.started_date = started_date;
    }


    public Peer getConversation_initiator() {
        return conversation_initiator;
    }

    public void setConversation_initiator(Peer conversation_initiator) {
        this.conversation_initiator = conversation_initiator;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public void setUnseenMessages(boolean unseenMessages) {
        this.unseenMessages = unseenMessages;
    }
    public boolean getUnseenMessage(){
        return unseenMessages;
    }
    public void setChatPartners(ArrayList<Peer> chat_partners){
        this.partners=chat_partners;
    }
    public ArrayList<Peer> getChatPartner(){
        return partners;
    }
    public void setMessages(ArrayList<Message> messages){
        this.messages=messages;
    }

    public static void getAllConversations(){
        DbHandler db=new DbHandler();
        ArrayList<Conversation> allConversations=db.getAllConversationsWithNewMessages();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public ArrayList<Message> getMessages(){
        return messages;
    }

    public String getUDPSeqNum() {
        return UDPSeqNum;
    }

    public void setUDPSeqNum(String UDPSeqNum) {
        this.UDPSeqNum = UDPSeqNum;
    }

    public long getSentTimeOfConversationinMillis() {
        return sentTimeOfConversationinMillis;
    }

    public void setSentTimeOfConversationinMillis(long sentTimeOfConversationinMillis) {
        this.sentTimeOfConversationinMillis = sentTimeOfConversationinMillis;
    }
}

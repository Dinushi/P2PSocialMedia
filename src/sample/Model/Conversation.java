package sample.Model;

import sample.CommunicationHandler.HeartbeatAgent;
import sample.DBHandler.DbHandler;


import java.time.LocalDateTime;
import java.util.ArrayList;


public class Conversation {
    private ArrayList<Peer> partners;
    private ArrayList<Message> messages;
    private  LocalDateTime started_date;

    public Conversation(){
        partners=new ArrayList<>();
        messages=new ArrayList<>();
        this.setStarted_date(LocalDateTime.now());
        DbHandler db=new DbHandler();
        db.addNewConv(this);


    }
    private void addPartner(Peer p){
        this.partners.add(p);
    }
    public void createMessage(){
        Message msg=new Message("I will come");
        this.messages.add(msg);
        this.sentAMessage(msg);

    }
    private void sentAMessage(Message msg){




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
}

package sample.EventHandler;

import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.DBHandler.DbHandler;
import sample.Model.Conversation;
import sample.Model.Peer;

import java.util.ArrayList;

public class ConversationHandler {

    public static void sendTheInitialConversation(Conversation conv){
        addToDb(conv);
        PeerConnection peerConn = PeerConnection.getPeerConnection();
        ArrayList<ReceivingPeer> receivers = new ArrayList<>();

        for (Peer r_peer : conv.getChatPartner()) {
            System.out.println("A requesting Peer"+r_peer.getPort()+" "+r_peer.getIp());
            ReceivingPeer receiver = new ReceivingPeer(r_peer.getIp(), r_peer.getPort());
            receivers.add(receiver);
        }
        if(!receivers.isEmpty()) {
            peerConn.sendViaSocket(conv, receivers);
        }
    }


    private synchronized static void addToDb(Conversation conv){
        DbHandler db=new DbHandler();
        db.addNewConv(conv);
    }

    public synchronized static void gotAInitialConversation(Conversation conv){
        addToDb(conv);

        //need to add this as a new Conversation.

    }
    public static ArrayList<Conversation> getAllConversations(){
        DbHandler db=new DbHandler();
        ArrayList<Conversation> allConv=db.getAllConversationsWithNewMessages();
        db.closeConnection();
        return allConv;
    }

}

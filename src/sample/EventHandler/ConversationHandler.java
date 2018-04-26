package sample.EventHandler;

import javafx.application.Platform;
import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Controller.ChatController;
import sample.DBHandler.DbHandler;
import sample.Model.Conversation;
import sample.Model.Message;
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
        for(Peer p:conv.getChatPartner()){
            db.addChatPartnersToChatTable(conv.getConversation_id(),conv.getConversation_initiator().getUsername(),p.getUsername());
        }
        for(Message msg:conv.getMessages()){
            db.addAMessage(msg);
        }
        db.closeConnection();
    }

    public synchronized static void gotAInitialConversation(Conversation conv){
        System.out.println("Received conversation title"+conv.getTitle());
        System.out.println("Initiator username"+conv.getConversation_initiator().getUsername());
        System.out.println("conv_id"+conv.getConversation_id());

        conv.setTitle(conv.getConversation_initiator().getUsername());//This one should see the other peer's name as the title.
        System.out.println("Received conversation  new title"+conv.getTitle());
        addToDb(conv);
        if(ChatController.chatController!=null){
            ChatController.chatController.gotANewConversation(conv);
        }

    }

    public static ArrayList<Conversation> getAllConversations(){
        DbHandler db=new DbHandler();
        ArrayList<Conversation> allConv=db.getAllConversationsWithNewMessages();
        db.closeConnection();
        return allConv;
    }
    public static void sendMessageToPartners(Conversation conv,Message msg){
        addMsgToDb(msg);
        System.out.println("came here laaa laaa sending a message to the chin app");
        System.out.println("sent message conv_id  "+msg.getConversation_id());
        System.out.println("Initiator username"+msg.getConversation_initiator().getUsername());
        System.out.println("sent  message msg_id  "+msg.getMessage_id());
        System.out.println("sent  message creator "+msg.getMsg_creator());
        System.out.println("msg contente"+msg.getContent());
        PeerConnection peerConn = PeerConnection.getPeerConnection();
        ArrayList<ReceivingPeer> receivers = new ArrayList<>();

        for (Peer r_peer : conv.getChatPartner()) {
            System.out.println("A requesting Peer"+r_peer.getPort()+" "+r_peer.getIp());
            ReceivingPeer receiver = new ReceivingPeer(r_peer.getIp(), r_peer.getPort());
            receivers.add(receiver);
        }
        if(!receivers.isEmpty()) {
            peerConn.sendViaSocket(msg, receivers);
        }
    }

    private synchronized static void addMsgToDb(Message msg){
        DbHandler db=new DbHandler();
        db.addAMessage(msg);
        db.closeConnection();
    }
    public synchronized static void gotAMessage(Message msg){
        System.out.println("came here laaa laaa getting a message from the chin app");
        System.out.println("Received message conv_id  "+msg.getConversation_id());
        System.out.println("Initiator username"+msg.getConversation_initiator().getUsername());
        System.out.println("Received message msg_id  "+msg.getMessage_id());
        System.out.println("Received message creator "+msg.getMsg_creator());
        System.out.println("msg contente"+msg.getContent());

        msg.setSent_received("R");
        msg.setStatus("U");
        DbHandler db=new DbHandler();
        Conversation conv=db.getAConversation(msg.getConversation_id(),msg.getConversation_initiator().getUsername());
        if(conv!=null){
            conv.addMessage(msg);
            addMsgToDb(msg);
            db.closeConnection();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(ChatController.chatController!=null){
                        ChatController.chatController.createNewConversationTab(conv,false);
                    }
                }
            });
            //if(ChatController.chatController!=null){
            //ChatController.chatController.createNewConversationTab(conv,false);
            //}



        }else{
            System.out.println("A wrong packet has received");
        }




        //need to add this as a new Conversation.

    }
}

package sample.EventHandler;

import javafx.application.Platform;
import sample.CommunicationHandler.PeerConnection;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Controller.ChatController;
import sample.Controller.Validator;
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
//wgen adding a new conversation to db,its first message is also added here with
    private synchronized static void addToDb(Conversation conv){
        DbHandler db=new DbHandler();
        System.out.println("@@@@@@@@@@@@@@@@Adding vonv to db");
        boolean result=db.addNewConv(conv);
        System.out.println("conv addition"+result);
        for(Peer p:conv.getChatPartner()){
            db.addChatPartnersToChatTable(conv.getConversation_id(),conv.getConversation_initiator().getUsername(),p.getUsername());
        }
        for(Message msg:conv.getMessages()){
            db.addAMessage(msg);
        }
        db.closeConnection();
    }

    public synchronized static void gotAInitialConversation(Conversation conv){
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@Received conversation title"+conv.getTitle());
        System.out.println("@@@@@@@@@@@@@@@@@Initiator username"+conv.getConversation_initiator().getUsername());
        System.out.println("@@@@@@@@@@@@@@@@conv_id"+conv.getConversation_id());
        System.out.println("chat member"+conv.getChatPartner().get(0).getUsername());
        //check whether the chat already exists
        DbHandler db=new DbHandler();
        int availability=db.removeAConversation(conv);
        db.closeConnection();

        if(availability==0){
            System.out.println("@@@@@@@@@@@@@@No old conve");
            //when a old chat is available under this chat creator
            if(conv.getChatPartner().size()==1){
                System.out.println("Conv size is 1");
                conv.setTitle(conv.getConversation_initiator().getUsername());//This one should see the other peer's name as the title.
                conv.getChatPartner().clear();
                conv.addPartner(conv.getConversation_initiator());
                System.out.println("Conv size now"+conv.getChatPartner().size());
            }else{
                ArrayList<Peer> tempartners=new ArrayList<>();

                for (Peer p:conv.getChatPartner()){
                    if(p.getUsername()!= Validator.thisPeer.getUsername()){
                        tempartners.add(p);
                    }
                }
                tempartners.add(conv.getConversation_initiator());
                conv.setChatPartners(tempartners);
            }
            System.out.println("Received conversation  new title"+conv.getTitle());
            addToDb(conv);
            if(ChatController.chatController!=null){
                ChatController.chatController.gotANewConversation(conv);
            }

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
        System.out.println("came here laaa laaa sending a message");
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
    public static synchronized void deleteConversation(Conversation conv){
        DbHandler db=new DbHandler();
        db.removeAConversation(conv);//method to delete all data regarding this chat
        db.closeConnection();
    }
    public static void informPartnersAboutChatDeletion(Conversation conv){
        sendTheInitialConversation(conv);
        //conversation is sent back again.Then receiver verifies if it is alrady known delete the conversation
    }

    public synchronized static boolean addMsgToDb(Message msg){
        DbHandler db=new DbHandler();
        boolean result=db.addAMessage(msg);
        db.closeConnection();
        return result;
    }
    public synchronized static void gotAMessage(Message msg){

        System.out.println("came here got got getting a message ");
        System.out.println("Received message conv_id  "+msg.getConversation_id());
        System.out.println("Initiator username"+msg.getConversation_initiator().getUsername());
        System.out.println("Received message msg_id  "+msg.getMessage_id());
        System.out.println("Received message creator "+msg.getMsg_creator());
        System.out.println("msg contente"+msg.getContent());

        msg.setSent_received("R");
        msg.setStatus("U");
        DbHandler db=new DbHandler();
        Conversation conv=db.getAConversation(msg.getConversation_id(),msg.getConversation_initiator().getUsername());
        //A message is aded to the db only if a conversation available
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

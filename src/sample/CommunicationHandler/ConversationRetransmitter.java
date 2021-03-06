package sample.CommunicationHandler;

import sample.Model.Conversation;
import sample.Model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConversationRetransmitter extends Thread {

    private static  ConversationRetransmitter conversationRetransmitter=null;
    private Map<String,Conversation> allSentConversation;
    private Map <String,ArrayList<ReceivingPeer>> receiversOfEachPacket;




    private ConversationRetransmitter (){
        this.allSentConversation= new HashMap<>();
        receiversOfEachPacket=new HashMap<>();
    }

    public static ConversationRetransmitter  getConversationRetransmitter (){
        if(conversationRetransmitter==null) {
            conversationRetransmitter = new ConversationRetransmitter();
            return conversationRetransmitter;
        }else{
            return conversationRetransmitter;
        }
    }

    //keep the message until it is acknowledged
    public Map addAConversation(Conversation conv,ArrayList<ReceivingPeer> peerIpPorts){
        allSentConversation.put(String.valueOf(conv.getUDPSeqNum()),conv);
        receiversOfEachPacket.put(String.valueOf(conv.getUDPSeqNum()),peerIpPorts);
        return receiversOfEachPacket;
    }

    //called by packet handler when a ACK for the conversation is received
    public Conversation  gotAAckForaConversation(String seq_num,ArrayList<ReceivingPeer> theReceiverWhoAcknowledged){
        System.out.println("Received Ack for conv%%%%%%%%%%%%%%%%%%%");
        int noOfconvReceivers =0;
        try {
            noOfconvReceivers = this.receiversOfEachPacket.get(seq_num).size();
            System.out.println("noOfconvReceivers of the con"+noOfconvReceivers);
        }catch(NullPointerException ex){
            //This is a Lately received Ack. We are already re_transmitted the relevent conversation
            return null;
        }
        if(noOfconvReceivers==1){
            //only one chat partner.He os acksnowleded
            System.out.println("removing the one & only prtner");
            this.receiversOfEachPacket.remove(seq_num);
            return this.allSentConversation.remove(seq_num);

        }else{
            //when several peers are connected with the chat
            for(ReceivingPeer r_peer:this.receiversOfEachPacket.get(seq_num)){
                if(r_peer.getIP().equals(theReceiverWhoAcknowledged.get(0).getIP() ) && r_peer.getPort()==theReceiverWhoAcknowledged.get(0).getPort()){
                    receiversOfEachPacket.get(seq_num).remove(r_peer);
                    //test the above for proper performance
                    break;
                }
            }
            return null;
        }
    }

    public void run(){
        while(true){
            try {
                //allow the thread  to resend conversations if no ACK is received.
                Thread.sleep(180000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long current_time=System.currentTimeMillis();
            for (Conversation conv : allSentConversation.values())
                if(current_time-conv.getSentTimeOfConversationinMillis()>40000){
                    //the message is retransmitted
                    if(this.receiversOfEachPacket.get(conv.getUDPSeqNum())!=null){
                        PeerConnection.getPeerConnection().sendViaSocket(conv,this.receiversOfEachPacket.get(conv.getUDPSeqNum()));
                    }
                    //old entries are removed
                    this.receiversOfEachPacket.remove(conv.getUDPSeqNum());
                    this.allSentConversation.remove(conv.getUDPSeqNum());
                }

        }

    }
}

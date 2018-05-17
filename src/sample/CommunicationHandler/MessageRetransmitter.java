package sample.CommunicationHandler;

import sample.Model.Message;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageRetransmitter extends Thread{
    private static  MessageRetransmitter msgRetransmiiter=null;
    private Map <String,Message  > allSentMesages;
    private Map <String,ArrayList<ReceivingPeer>> receiversOfEachPacket;




    private MessageRetransmitter(){
        this.allSentMesages= new HashMap<>();
        receiversOfEachPacket=new HashMap<>();
    }

    public static MessageRetransmitter getMessageRetransmitter(){
        if(msgRetransmiiter==null) {
            msgRetransmiiter = new MessageRetransmitter();
            return msgRetransmiiter;
        }else{
            return msgRetransmiiter;
        }
    }

    //keep the message until it is acknowledged
    public Map addAMessage(Message msg,ArrayList<ReceivingPeer> peerIpPorts){
        //System.out.println("Adding the message");
        //System.out.println(msg);
        //System.out.println("message gotter"+peerIpPorts.get(0).getIP());
        //System.out.println("msg_seg_Num"+msg.getUDPSeqNum());
        allSentMesages.put(msg.getUDPSeqNum(),msg);
        receiversOfEachPacket.put(msg.getUDPSeqNum(),peerIpPorts);
        return receiversOfEachPacket;
    }

    public Message gotAAckForaMessage(String seq_num,ArrayList<ReceivingPeer> theReceiverWhoAcknowledged){
        int noOfMsgReceivers =0;
        try {
            noOfMsgReceivers = this.receiversOfEachPacket.get(seq_num).size();

        }catch(NullPointerException ex){
            //This is a Lately received Ack. We are already re_transmitted the relevent message
            return null;
        }
        if(noOfMsgReceivers == 1){
            this.receiversOfEachPacket.remove(seq_num);
            return this.allSentMesages.remove(seq_num);
        }else{
            for(ReceivingPeer r_peer:this.receiversOfEachPacket.get(seq_num)){
                if(r_peer.getIP().equals(theReceiverWhoAcknowledged.get(0).getIP()) && r_peer.getPort()==theReceiverWhoAcknowledged.get(0).getPort()){
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
                System.out.println("started Retransmitter thread");
                Thread.sleep(90000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long current_time=System.currentTimeMillis();
            System.out.println("going to resend a message");
            for (Message msg : allSentMesages.values())
                if(current_time-msg.getSentTimeInMillis()>40000){
                    //the message is retransmitted
                    System.out.println("ahh no ack got for this msg");
                    System.out.println(receiversOfEachPacket.get(msg.getUDPSeqNum()).get(0));
                    if(this.receiversOfEachPacket.get(msg.getUDPSeqNum())!=null){
                        PeerConnection.getPeerConnection().sendViaSocket(msg,this.receiversOfEachPacket.get(msg.getUDPSeqNum()));
                    }

                    //old entries are removed
                    this.receiversOfEachPacket.remove(msg.getUDPSeqNum());
                    this.allSentMesages.remove(msg.getUDPSeqNum());
                }

        }

    }
}

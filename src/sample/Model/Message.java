package sample.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private int conversation_id;
    private Peer conversation_initiator;
    private int message_id;
    private String msg_creator;
    private String content;
    private String sent_received;//sent/received
    private String status;//delivered/notDelivered/seen/unseen
    private LocalDateTime sent_time;

    public Message(String msg_creator,String content){
        this.msg_creator=msg_creator;
        this.content=content;
        this.setSent_time(LocalDateTime.now());
    }


    public LocalDateTime getSent_time() {
        return sent_time;
    }

    public void setSent_time(LocalDateTime sent_time) {
        this.sent_time = sent_time;
    }

    public String getSent_received() {
        return sent_received;
    }

    public void setSent_received(String sent_received) {
        this.sent_received = sent_received;
    }

    public String getStatus() {
        return status;
    }
    public String getContent() {
        return content;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }
    public String getMsg_creator(){
        return msg_creator;
    }

    public Peer getConversation_initiator() {
        return conversation_initiator;
    }

    public void setConversation_initiator(Peer conversation_initiator) {
        this.conversation_initiator = conversation_initiator;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }
}

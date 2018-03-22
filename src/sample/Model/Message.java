package sample.Model;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private InetAddress senderIp;
    private int senderPort;
    private String content;
    private LocalDateTime sent_time;

    public Message(String content){
        this.content=content;
        this.sent_time= LocalDateTime.now();

    }



}

package sample.Model;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Peer extends AbstractPeer{
    private  boolean isJoined;
    private LocalDateTime joinedDate;
    private boolean onlineStatus;


    public Peer(String username,InetAddress ip,int port,boolean isJoined) {

        super(username,ip,port);
        this.setJoined(isJoined);
        if(isJoined){
            this.setJoinedDate();
        }
        // this.onlineStatus=this.checkOnlineStatus(ip,port);
    }


    private void setJoinedDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        joinedDate=now;
    }

    //if the peer is confirmed after creating the object
    public void setJoined(boolean joined) {
        isJoined = joined;
        if(isJoined){
            this.setJoinedDate();
        }
    }

    public boolean isJoined() {
        return isJoined;
    }
    public LocalDateTime getJoinedDate() {
        return joinedDate;
    }
    public boolean getOnlineStatus() {
        return onlineStatus;
    }
    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /*
    public boolean checkOnlineStatus(InetAddress IP,int port){
       if(){
            return true;
        } else{
            return false;
        }

    }
    */
}

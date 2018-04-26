package sample.Model;

import sample.DBHandler.DbHandler;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class Peer implements Serializable {
    private String username;
    private  InetAddress ip;
    private int port;
    private String fullname;
    private   String status;
    private   String gender;
    private Date bday;
    //private String prof_pic;
    private String hometown;
    private  boolean isJoined;
    private LocalDateTime joinedDate;
    private boolean onlineStatus;
    private byte[] prof_pic;


    public Peer(String username,InetAddress ip,int port,boolean isJoined) {
        this.username=username;
        this.ip=ip;
        this.port=port;
        this.setJoined(isJoined);
        if(isJoined){
            this.setJoinedDate();
        }
        this.onlineStatus=true;
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
    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
       if (!(o instanceof Peer)) {
            return false;
        }
        Peer peer = (Peer) o;
        return username == peer.getUsername() &&
               Objects.equals(ip, peer.getIp()) &&
                Objects.equals(port, peer.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, ip, port);
   }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBday() {
        return bday;
    }

    public void setBday(Date bday) { this.bday = bday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean insertToDb(){
        DbHandler db=new DbHandler();
        boolean result= db.addAnewPeer(this);
        db.closeConnection();
        return result;
    }
    public boolean updateprofiledata(){
        DbHandler db=new DbHandler();
        boolean result= db.updatePeerDataChanges(this);
        db.closeConnection();
        return result;
    }
    public static Peer retrieveAPeer(String username){
        DbHandler db=new DbHandler();
        Peer peer= db.getPeer(username);
        db.closeConnection();
        return peer;
    }

    public byte[] getProf_pic() {
        return prof_pic;
    }

    public void setProf_pic(byte[] prof_pic) {
        this.prof_pic = prof_pic;
    }
}

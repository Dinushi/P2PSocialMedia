package sample.Model;

import java.net.InetAddress;

//make this class singleton

public class ThisPeer extends AbstractPeer{
    private String password;

    public ThisPeer(String username,InetAddress ip,int port,String password){
        super(username,ip,port);
        this.setPassword("123");
        //this.password=password;
        //
        //this.updateDatabase();


    }

    public void updateDatabase(){

        System.out.println("Successfully Updated the Database");

    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

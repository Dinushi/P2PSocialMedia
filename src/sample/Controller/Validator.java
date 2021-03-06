package sample.Controller;

import sample.DBHandler.DbHandler;
import sample.Model.Owner;
import sample.Model.Peer;
import sample.Model.ThisPeer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Validator {

    //ArrayList<String> userDetails;
    public static Peer thisPeer=null;
    private String[] userCredentials;
    private InetAddress myIp;
    private int myPort;
    public static String username;
    public static LocalDateTime last_logOuttime;

    public int validateUser(String username_entered,String password_entered){

        //user credentials are read
        readTheUserDataFile();

        String username_stored=userCredentials[0];
        String password_stored=userCredentials[3];
        //String Strlast_logout_time=userCredentials[4];

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //last_logOuttime = LocalDateTime.parse(Strlast_logout_time, formatter);

        //password_stored="123";
        try {
            if (username_entered.contentEquals(username_stored)) {
                if (password_entered.contentEquals(password_stored)) {
                    try {
                        this.myIp = InetAddress.getByName(userCredentials[1]);
                        this.myPort = Integer.parseInt(userCredentials[2]);
                        //this.myPort=9877;

                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    System.out.println("user is valid");
                    username = username_stored;//this done temporalarily to get username when required.

                    //setting the static variable to cratte the socket listner

                    DbHandler db = new DbHandler();

                    Peer myself = db.getPeer(username);
                    Owner.myUsername = username;
                    Owner.myIP = myself.getIp();
                    Owner.myPort = myself.getPort();
                    db.closeConnection();


                    return 0;
                } else {
                    System.out.println("user password wrong");
                    return 1;
                }
            } else {
                System.out.println("username is incorrect");
                return 2;
            }
        }catch(NullPointerException ex){
            return 4;
        }
    }
    public InetAddress getMyIp(){
        return this.myIp;
    }
    public int getMyPort(){
        return this.myPort;
    }

    private void readTheUserDataFile(){
        //this.userDetails=new ArrayList();
        userCredentials = new String[5];
        // The name of the file to open.
        String fileName = "ThisUser.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            int i=0;
            while((line = bufferedReader.readLine()) != null) {
                userCredentials[i]=line.replaceAll("[\n\r]", "");
                i++;
            }
            bufferedReader.close();
            fileReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }

    }
}

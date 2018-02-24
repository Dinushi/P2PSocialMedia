package sample.Controller;

import sample.Model.ThisPeer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Validator {

    //ArrayList<String> userDetails;
    private String[] userCredentials;
    private InetAddress myIp;
    private int myPort;

    public int validateUser(String username_entered,String password_entered){


        //user credentials are read
        readTheUserDataFile();
        String username_stored=userCredentials[0];
        String password_stored=userCredentials[1];

        if (username_entered.contentEquals(username_stored)) {
            if (password_entered.contentEquals(password_stored)) {
                try {
                    this.myIp=InetAddress.getByName(userCredentials[2]);
                    this.myPort=Integer.parseInt(userCredentials[3]);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                System.out.println("user is valid");
                return 0;
            }else{
                System.out.println("user password wrong");
                return 1;
            }
        }else{
            System.out.println("username is incorrect");
            return 2;
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
package sample.CommunicationHandler;

import sample.EventHandler.NewPeerListner;
import sample.Model.DiscoverdPeer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

// A simple Client Server Protocol ..

public class ClientConnection {
    private InetAddress bs_address=null;
    static  final int bs_port_no=55554;
    private Socket socket=null;

    private InetAddress local_address;
    private int local_port;

    InputStream is1;//**********
    ObjectInputStream ois;//***********
    OutputStream os1;//***********
    ObjectOutputStream oos;//********


    //public class NetworkClient {

    //public static void main(String args[]) throws IOException{

    public ClientConnection(String username){//create  a singleton client class in any case of user name error ame client object can be used
        try {
            this.bs_address=InetAddress.getLocalHost();
            this.socket=new Socket(bs_address, bs_port_no);//find a way to set bs address,and port in the program input
            this.local_address=socket.getLocalAddress();
            this.local_port = socket.getLocalPort();

            //this.local_address= s1.getLocalAddress();//insert these values to user table in database
            //this.local_port=s1.getLocalPort();

            this.os1 = socket.getOutputStream();//****
            this.oos = new ObjectOutputStream(os1);//***********

            this.is1 = socket.getInputStream();//********
            this.ois = new ObjectInputStream(is1);//************




            //br= new BufferedReader(new InputStreamReader(System.in));
            //is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
            //os= new PrintWriter(s1.getOutputStream());

            SendToBS(username);
            receiveFromBs();

            //createTheUserCredentials(username);
            System.out.println("Server Connection closed");
            this.socket.close();this.ois.close();this.is1.close();this.os1.close();this.oos.close();
            if(this.socket.isClosed()){
                System.out.println("yes socket closed");
            }else{
                System.out.println("No socket not closed");
            }


        }catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
    }
    private void receiveFromBs(){
            try {
                String reply=(String)this.ois.readObject();
                System.out.println("Reply"+reply);

                if(reply.contentEquals("UserName Exists")) {
                    //ask to enter a new username
                }else if(reply.contentEquals("IP Port Already Used")){
                    //system must automatically change the port
                }else if(reply.substring(0,reply.length()-1).contentEquals("Success")) {
                    int i=0;
                        while(i<Integer.parseInt(reply.substring(reply.length()-1))) {
                            DiscoverdPeer neighbour_peer = (DiscoverdPeer) this.ois.readObject();
                            NewPeerListner.update_PeersSentByBS(neighbour_peer);
                            System.out.println("Received A new peer");
                            System.out.println(neighbour_peer.getUsername()+" "+neighbour_peer.getIp()+" "+neighbour_peer.getPort());
                            i++;
                            //if(reply.contentEquals("End")){
                               // return;
                            //}
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }
    private void SendToBS(String username){
        try {
            DiscoverdPeer peer=new DiscoverdPeer("new join request",username,local_address,local_port);
            //DiscoverdPeer peer1=(DiscoverdPeer)this.ois.readObject();//**************8//read objects from client

            this.oos.writeObject(peer);
            this.oos.flush();
            System.out.println("Mytry at the moment");

            //System.out.println(this.ois.readObject());
            //this.oos.writeObject(new String("another object from the client"));

            System.out.println("Client Address : " + this.bs_address);
            //os.println(this.username);
            //os.flush();
            //this.response = is.readLine();
            //System.out.println("Server Response : " + response);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
            //}catch (ClassNotFoundException e) {//******************
            //e.printStackTrace();
        }finally{
            //after all communication done close this socket
            // is.close();os.close();br.close();s1.close();
            //System.out.println("Connection Closed");
        }
            /*
            //System.out.println("Enter Data to Server ( Enter QUIT to end):");\

            try{
                line=br.readLine();
                while(line.compareTo("QUIT")!=0){
                    os.println(line);
                    os.flush();
                    response=is.readLine();
                    System.out.println("Server Response : "+response);
                    line=br.readLine();
                }
            } catch(IOException e){
                e.printStackTrace();
                System.out.println("Socket read Error");
            }finally{
                is.close();os.close();br.close();s1.close();
                System.out.println("Connection Closed");
            }
            */
    }

    public InetAddress getLocal_address() {
        return local_address;
    }
    public int getLocal_port() {
        return local_port;
    }


    private void createTheUserCredentials(String username) throws IOException{

            BufferedWriter writer = new BufferedWriter(new FileWriter("ThisUser.txt"));
            writer.write(username);
            writer.newLine();
            writer.write(String.valueOf(this.getLocal_address().toString().split("/")[1]));
            writer.newLine();
            writer.write(String.valueOf(this.getLocal_port()));
            writer.newLine();
            writer.close();
            System.out.print("user Credentials has added in the file");


    }

}


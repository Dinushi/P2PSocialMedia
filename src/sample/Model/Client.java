// A simple Client Server Protocol ..
package sample.Model;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private InetAddress bs_address=null;
    static  final int bs_port_no=55554;
    private Socket socket=null;
    private String line=null;
    private BufferedReader br=null;
    private BufferedReader is=null;
    private PrintWriter os=null;
    private String response=null;
    private InetAddress local_address;
    private int local_port;
    InputStream is1;//**********
    ObjectInputStream ois;//***********
    OutputStream os1;//***********
    ObjectOutputStream oos;//********


    //public class NetworkClient {

        //public static void main(String args[]) throws IOException{

        public Client(String username){//create  a singleton client class in any case of user name error ame client object can be used
            try {
                this.bs_address=InetAddress.getLocalHost();
                this.socket=new Socket(bs_address, bs_port_no);

                this.local_address=socket.getLocalAddress();//insert these values to user table in database
                this.local_port=socket.getLocalPort();


                //this.is1 = socket.getInputStream();//********
                //this.ois = new ObjectInputStream(is1);//************


                this.os1 = socket.getOutputStream();//****
                this.oos = new ObjectOutputStream(os1);//***********
                //br= new BufferedReader(new InputStreamReader(System.in));
                //is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
                //os= new PrintWriter(s1.getOutputStream());

                communicate(username);
            }catch (IOException e){
                e.printStackTrace();
                System.err.print("IO Exception");
            }
        }
        private void communicate(String username){
            try {
                System.out.println("tyrrrrrrrrrrr");
                DiscoverdPeer peer=new DiscoverdPeer("new join request",username,socket.getLocalAddress(),socket.getLocalPort());
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
               // is.close();os.close();br.close();s1.close();
            System.out.println("Connection Closed");
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

}

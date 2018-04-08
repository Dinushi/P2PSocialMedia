package sample.DBHandler;

import javafx.geometry.Pos;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Model.Conversation;
import sample.Model.DiscoverdPeer;
import sample.Model.Peer;
import sample.Model.Post;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DbHandler {
    private final String DB_URL = "jdbc:derby:MyDB";
    private Connection conn = null;
    private Statement stmt = null;

    public DbHandler() {
        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean addAnewPeer(Peer peer) {
        try {

            String template = "INSERT INTO PEER (USERNAME,IP,PORT,FULLNAME,STATUS,BIRTHDAY,GENDER,HOMETOWN,JOINED_STATUS) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1, peer.getUsername());
            stmt.setString(2, String.valueOf(peer.getIp()));
            stmt.setInt(3, peer.getPort());
            stmt.setString(4, peer.getFullname());
            stmt.setString(5, peer.getStatus());
            stmt.setDate(6, new java.sql.Date(peer.getBday().getTime()));
            stmt.setString(7, peer.getGender());
            stmt.setString(8, peer.getHometown());
            if(peer.isJoined()){
                stmt.setString(9, "T");
            }else{
                stmt.setString(9, "F");
            }
            stmt.executeUpdate();

            System.out.println("A peer has stored.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;
        }
    }

    public void addNewConversation() {


    }

    public synchronized Peer getPeer(String username) {
        PreparedStatement statement = null;
        Peer peer = null;
        try {
            statement = conn.prepareStatement("select * from PEER where USERNAME = ? ");
            statement.setString(1, String.valueOf(username));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            if (resultSet.next()) {
                peer = new Peer(resultSet.getString("USERNAME"), InetAddress.getByName(resultSet.getString("IP").substring(1)), resultSet.getInt("PORT"), true);
                System.out.println("Peer is created");
                peer.setFullname(resultSet.getString("FULLNAME"));
                peer.setStatus(resultSet.getString("STATUS"));
                peer.setBday(resultSet.getDate("BIRTHDAY"));
                peer.setGender(resultSet.getString("GENDER"));
                peer.setHometown(resultSet.getString("HOMETOWN"));
                if(resultSet.getString("JOINED_STATUS")=="F"){
                    peer.setJoined(false);
                }else{
                    peer.setJoined(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return peer;
    }
    public synchronized void updatePeerConfirmation(String username){
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("UPDATE PEER SET JOINED_STATUS=? WHERE USERNAME = ? ");
            statement.setString(1, "T");
            statement.setString(2, String.valueOf(username));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public synchronized void removeAPeer(String username){
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("delete from APP.PEER where USERNAME= ? ");
            statement.setString(1, username);
            boolean result= statement.execute();
            statement.clearParameters();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayList<Peer> getAllPeers(String joined_status) {
        PreparedStatement statement = null;
        ArrayList<Peer> allPeers=new ArrayList<>();
        Peer peer=null;
        try {
            statement = conn.prepareStatement("select * from PEER where JOINED_STATUS = ? ");
            statement.setString(1, String.valueOf(joined_status));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                peer = new Peer(resultSet.getString("USERNAME"), InetAddress.getByName(resultSet.getString("IP").substring(1)), resultSet.getInt("PORT"), true);
                System.out.println("Peer is created");
                peer.setFullname(resultSet.getString("FULLNAME"));
                peer.setStatus(resultSet.getString("STATUS"));
                peer.setBday(resultSet.getDate("BIRTHDAY"));
                peer.setGender(resultSet.getString("GENDER"));
                peer.setHometown(resultSet.getString("HOMETOWN"));
                allPeers.add(peer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return allPeers;
    }

    public synchronized ArrayList<String> selectAllPeerUsernames() {
        PreparedStatement statement = null;
        ArrayList<String> allPeerUsernames = new ArrayList<>();
        try {
            statement = conn.prepareStatement("select USERNAME from PEER");
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                System.out.println("came here");
                allPeerUsernames.add(resultSet.getString("USERNAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        System.out.print("At Db :" + allPeerUsernames);
        return allPeerUsernames;
    }

    public synchronized boolean addNewConv(Conversation conversation) {
        try {

            String template = "INSERT INTO APP.CONVERSATION (CONVERSATION_ID,STARTED_DATE) values (?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setInt(1, 1);
            stmt.setDate(2, java.sql.Date.valueOf(conversation.getStarted_date().toLocalDate()));
            stmt.executeUpdate();

            System.out.println("A peer has stored.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;
        }

    }

    public synchronized ArrayList<String> selectAllConversations() {
        PreparedStatement statement = null;
        ArrayList<String> allPeerUsernames = new ArrayList<>();
        try {
            statement = conn.prepareStatement("select * from APP.CONVERSATION");
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                System.out.println("came here");
                allPeerUsernames.add(resultSet.getString("USERNAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        System.out.print("At Db :" + allPeerUsernames);
        return allPeerUsernames;
    }

    public synchronized ArrayList<Post> getPosts() {
        PreparedStatement statement = null;
        ArrayList<Post> allPosts = new ArrayList<>();
        Post post = null;
        try {
            statement = conn.prepareStatement("select * from POST");
            //statement.setString(1, String.valueOf(username));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                post = new Post(resultSet.getString("USERNAME"), resultSet.getString("CONTENT"));
                System.out.println("Post is retrieved");
                post.setDate_created(resultSet.getTimestamp("CREATED_DATE").toLocalDateTime());
                allPosts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPosts;
    }

    public synchronized boolean addNewPost(Post post) {
        try {

            String template = "INSERT INTO APP.POST (USERNAME,POST_ID,CONTENT,CREATED_DATE) values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1, post.getUsername());
            stmt.setInt(2, 3);
            stmt.setString(3, post.getContent());
            stmt.setDate(4, java.sql.Date.valueOf(post.getDate_created().toLocalDate()));
            stmt.executeUpdate();

            System.out.println("A peer has stored.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;
        }
    }

    public synchronized ArrayList<ReceivingPeer> selectAllPeerAddresses(String joined_status) {
        PreparedStatement statement = null;
        ArrayList<ReceivingPeer> allPeerAdresses = new ArrayList<>();
        try {
            statement = conn.prepareStatement("select IP,PORT from PEER where JOINED_STATUS = ? ");
            statement.setString(1, String.valueOf(joined_status));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                //String[] peerData=new String[2];
                String IP = resultSet.getString("IP");
                int port = resultSet.getInt("PORT");
                ReceivingPeer receiver = new ReceivingPeer(InetAddress.getByName(IP), port);
                // peerData[0]=resultSet.getString("IP");
                //peerData[1]=resultSet.getString("PORT");
                allPeerAdresses.add(receiver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.print("At Db :" + allPeerAdresses);
        return allPeerAdresses;
    }

    public synchronized void addNewDiscoverdPeer(DiscoverdPeer peer,String requested) {
        try {

            String template = "INSERT INTO APP.DISCOVERD_PEERS(USERNAME,IP,PORT,REQUESTED) values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1, peer.getUsername());
            stmt.setString(2, String.valueOf(peer.getIp()));
            stmt.setInt(3, peer.getPort());
            stmt.setString(4,requested);
            stmt.executeUpdate();

            System.out.println("Msg from DBHandler/Inserting peers from BS:The requested peer has stored in the DB.");

        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());

        }
    }
    //below 2 are wrong tables

    public synchronized ArrayList<DiscoverdPeer> getAllReceivedPeers() {
        PreparedStatement statement = null;
        ArrayList<DiscoverdPeer> peersToRequest = new ArrayList<>();
        try {
            statement = conn.prepareStatement("select * from APP.PEER_DATA ");
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                String username = resultSet.getString("USERNAME");
                String IP = resultSet.getString("IP");
                int port = resultSet.getInt("PORT");
                DiscoverdPeer dPeer = new DiscoverdPeer("Join", username, InetAddress.getByName(IP), port);
                peersToRequest.add(dPeer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return peersToRequest;
    }
    public synchronized boolean removeAdiscoverdPeer(String username){
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("delete from APP.DISCOVERD_PEERS where USERNAME= ? ");
            statement.setString(1, username);
            boolean result= statement.execute();
            statement.clearParameters();
            if(result)
                return true;
            else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

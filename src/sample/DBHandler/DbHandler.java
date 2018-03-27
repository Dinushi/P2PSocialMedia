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

    public boolean addAnewPeer(Peer peer) {
        try {

            String template = "INSERT INTO PEER (USERNAME,IP,PORT,FULLNAME,STATUS,BIRTHDAY,GENDER,HOMETOWN) values (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1, peer.getUsername());
            stmt.setString(2, String.valueOf(peer.getIp()));
            stmt.setInt(3, peer.getPort());
            stmt.setString(4, peer.getFullname());
            stmt.setString(5, peer.getStatus());
            stmt.setDate(6, new java.sql.Date(peer.getBday().getTime()));
            stmt.setString(7, peer.getGender());
            stmt.setString(8, peer.getHometown());
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

    public Peer getPeer(String username) {
        PreparedStatement statement = null;
        Peer peer = null;
        try {
            statement = conn.prepareStatement("select * from PEER where USERNAME = ? ");
            statement.setString(1, String.valueOf(username));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            if (resultSet.next()) {
                peer = new Peer(resultSet.getString("USERNAME"), InetAddress.getByName(resultSet.getString("IP")), resultSet.getInt("PORT"), true);
                System.out.println("Peer is created");
                peer.setFullname(resultSet.getString("FULLNAME"));
                peer.setStatus(resultSet.getString("STATUS"));
                peer.setBday(resultSet.getDate("BIRTHDAY"));
                peer.setGender(resultSet.getString("GENDER"));
                peer.setHometown(resultSet.getString("HOMETOWN"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return peer;
    }

    public ArrayList<String> selectAllPeerUsernames() {
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

    public boolean addNewConv(Conversation conversation) {
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

    public ArrayList<String> selectAllConversations() {
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

    public ArrayList<Post> getPosts() {
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

    public boolean addNewPost(Post post) {
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

    public ArrayList<ReceivingPeer> selectAllPeerAddresses() {
        PreparedStatement statement = null;
        ArrayList<ReceivingPeer> allPeerAdresses = new ArrayList<>();
        try {
            statement = conn.prepareStatement("select IP,PORT from PEER");
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

    public void addNewReceivedPeer(DiscoverdPeer peer) {
        try {

            String template = "INSERT INTO APP.PEER_DATA (USERNAME,IP,POST) values (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1, peer.getUsername());
            stmt.setString(2, String.valueOf(peer.getIp()));
            stmt.setInt(3, peer.getPort());
            stmt.executeUpdate();

            System.out.println("A received peer has stored has stored.");

        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());

        }
    }

    public ArrayList<DiscoverdPeer> getAllReceivedPeers() {
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
    public boolean removeAdiscoverdPeer(DiscoverdPeer peer){
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("delete from APP.PEER_DATA where USERNAME= ? ");
            statement.setString(1, peer.getUsername());
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

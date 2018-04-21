package sample.DBHandler;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import sample.CommunicationHandler.ReceivingPeer;
import sample.Controller.AlertHelper;
import sample.Controller.LoginController;
import sample.Model.*;

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
            if (peer.isJoined()) {
                stmt.setString(9, "T");
            } else {
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
                if (resultSet.getString("JOINED_STATUS") == "F") {
                    peer.setJoined(false);
                } else {
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

    public synchronized boolean updatePeerConfirmation(String username) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("UPDATE PEER SET JOINED_STATUS=? WHERE USERNAME = ? ");
            statement.setString(1, "T");
            statement.setString(2, username);
            boolean result = statement.execute();
            statement.clearParameters();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public synchronized void removeAPeer(String username) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("delete from APP.PEER where USERNAME= ? ");
            statement.setString(1, username);
            boolean result = statement.execute();
            statement.clearParameters();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayList<Peer> getAllPeers(String joined_status) {
        PreparedStatement statement = null;
        ArrayList<Peer> allPeers = new ArrayList<>();
        Peer peer = null;
        try {
            statement = conn.prepareStatement("select * from PEER where JOINED_STATUS = ? AND USERNAME <> ?");
            statement.setString(1, String.valueOf(joined_status));
            statement.setString(2, String.valueOf(Owner.myUsername));
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
            statement = conn.prepareStatement("select USERNAME from PEER WHERE USERNAME<>? AND JOINED_STATUS=?");
            statement.setString(1, String.valueOf(Owner.myUsername));
            statement.setString(2, String.valueOf("T"));
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
            //statement = conn.prepareStatement("select * from POST WHERE USERNAME <> ?");
            statement = conn.prepareStatement("select * from POST ");
            //statement.setString(1, String.valueOf(Owner.myUsername));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                post = new Post(resultSet.getString("USERNAME"), resultSet.getString("CONTENT"), resultSet.getInt("POST_ID"));
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
            stmt.setInt(2, post.getPostID());
            stmt.setString(3, post.getContent());
            stmt.setDate(4, java.sql.Date.valueOf(post.getDate_created().toLocalDate()));
            stmt.executeUpdate();

            System.out.println("A post has stored.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;
        }
    }

    public int getMyMaxPostID() {
        PreparedStatement statement = null;
        int post_ID = 0;
        try {
            statement = conn.prepareStatement("select MAX(POST_ID) from POST WHERE USERNAME=? GROUP BY USERNAME");
            statement.setString(1, String.valueOf(Owner.myUsername));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            if (resultSet.next()) {
                post_ID = resultSet.getInt("POST_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post_ID;
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
                ReceivingPeer receiver = new ReceivingPeer(InetAddress.getByName(IP.substring(1)), port);
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

    public synchronized boolean addNewDiscoverdPeer(DiscoverdPeer peer, String requested) {
        try {

            String template = "INSERT INTO APP.DISCOVERD_PEERS(USERNAME,IP,PORT,REQUESTED) values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1, peer.getUsername());
            stmt.setString(2, String.valueOf(peer.getIp()));
            stmt.setInt(3, peer.getPort());
            stmt.setString(4, requested);
            int result = stmt.executeUpdate();
            System.out.println("Dis_peer addition : " + result);
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;

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

    public synchronized int removeAdiscoverdPeer(String username) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("delete from APP.DISCOVERD_PEERS where USERNAME= ? ");
            statement.setString(1, username);
            int result = statement.executeUpdate();
            statement.clearParameters();
            System.out.println("Dis_peer removal : " + result);
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public synchronized boolean addNewReply(Reply reply) {
        try {

            String template = "INSERT INTO APP.REPLY (USERNAME,POST_ID,REPLY_ID,CONTENT,CREATED_DATE) values (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1, reply.getUsername());
            stmt.setInt(2, reply.getPost_id());
            stmt.setInt(3, reply.getReply_id());
            stmt.setString(4, reply.getContent());
            stmt.setDate(5, java.sql.Date.valueOf(reply.getDate_created().toLocalDate()));
            stmt.executeUpdate();

            System.out.println("A Reply has stored.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;
        }
    }

    public int getMyMaxReplyID(int post_id) {
        PreparedStatement statement = null;
        int reply_ID = 0;
        try {

            statement = conn.prepareStatement("select MAX(REPLY_ID) from APP.REPLY WHERE USERNAME=? AND POST_ID=? GROUP BY USERNAME,POST_ID");
            statement.setString(1, String.valueOf(Owner.myUsername));
            statement.setInt(2, post_id);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            if (resultSet.next()) {
                reply_ID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reply_ID;


    }

    public synchronized ArrayList<Reply> getReplies(int post_id) {
        PreparedStatement statement = null;
        ArrayList<Reply> allReplies = new ArrayList<>();
        Reply reply = null;
        try {
            statement = conn.prepareStatement("select * from REPLY WHERE POST_ID = ?");
            statement.setInt(1, post_id);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                reply = new Reply(resultSet.getString("USERNAME"), resultSet.getString("CONTENT"), resultSet.getInt("POST_ID"), resultSet.getInt("REPLY_ID"));
                System.out.println("Reply is retrieved");
                reply.setDate_created(resultSet.getTimestamp("CREATED_DATE").toLocalDateTime());
                allReplies.add(reply);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allReplies;
    }

    public synchronized ArrayList<Conversation> getAllConversationsWithNewMessages() {
        PreparedStatement statement = null;
        ArrayList<Conversation> allConversations = new ArrayList<>();
        Conversation conv = null;
        try {
            statement = conn.prepareStatement("select * from CONVERSATION");
            //statement = conn.prepareStatement("select DISTINCT CONVERSATION_ID, CONVERSATION_INITIATOR from MESSAGE where MSG_TYPE = ? AND MSG_STATUS = ?");
            //statement.setString(1, "Y");
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                conv = new Conversation();
                conv.setConversation_id(resultSet.getInt("CONVERSATION_ID"));
                conv.setConversation_initiator(this.getPeer(resultSet.getString("CONVERSATION_INITIATOR")));
                conv.setStarted_date(resultSet.getTimestamp("STARTED_DATE").toLocalDateTime());
                conv.setTitle(resultSet.getString("CONV_TITLE"));
                if (resultSet.getString("NEW_RECEIVED_MESSAGES") == "T") {
                    conv.setUnseenMessages(true);
                } else {
                    conv.setUnseenMessages(false);
                }
                conv.setChatPartners(this.getChatPartners(conv.getConversation_id(), conv.getConversation_initiator().getUsername()));
                conv.setMessages(this.getMessages(conv.getConversation_id(), conv.getConversation_initiator().getUsername()));
                allConversations.add(conv);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return allConversations;
    }


    public ArrayList<Peer> getChatPartners(int conversation_id, String conversation_initiator) {
        PreparedStatement statement = null;
        ArrayList<Peer> chatPartners = new ArrayList<>();
        Peer peer;
        try {
            statement = conn.prepareStatement("select * from CHAT where CONVERSATION_ID = ? AND CONVERSATION_INITIATOR =? ");
            statement.setInt(1, conversation_id);
            statement.setString(2, conversation_initiator);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                //String[] peerData=new String[2];
                String partner_username = resultSet.getString("PARTNER");
                peer = this.getPeer(partner_username);
                chatPartners.add(peer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatPartners;

    }

    public ArrayList<Message> getMessages(int conversation_id, String conversation_initiator) {
        PreparedStatement statement = null;
        ArrayList<Message> messages = new ArrayList<>();
        Message msg;
        try {
            statement = conn.prepareStatement("select * from MESSAGE where CONVERSATION_ID = ? AND CONVERSATION_INITIATOR =? ORDER BY TIME  ");
            statement.setInt(1, conversation_id);
            statement.setString(2, conversation_initiator);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                msg = new Message(resultSet.getString("MESSAGE_CREATOR"), resultSet.getString("CONTENT"));
                msg.setSent_time(resultSet.getTimestamp("TIME").toLocalDateTime());
                String sent_received = resultSet.getString("MSG_TYPE");
                if (sent_received == "S") {
                    msg.setSent_received("Sent");
                } else {
                    msg.setSent_received("Received");
                }
                String status = resultSet.getString("MSG_STATUS");
                if (sent_received == "S") {
                    msg.setStatus("Seen");
                } else if (sent_received == "U") {
                    msg.setStatus("Unseen");
                } else if (sent_received == "D") {
                    msg.setStatus("Delivered");
                } else {
                    msg.setStatus("NotDelivered");
                }

                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;

    }

    public int getMaxConvID() {
        PreparedStatement statement = null;
        int max_conv_ID = 0;
        try {

            statement = conn.prepareStatement("select MAX(CONVERSATION_ID) from APP.CONVERSATION WHERE CONVERSATION_INITIATOR=? GROUP BY CONVERSATION_INITIATOR ");
            statement.setString(1, String.valueOf(Owner.myUsername));
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            if (resultSet.next()) {
                max_conv_ID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max_conv_ID;


    }

    public int getMaxMsgId(Conversation conv) {
        PreparedStatement statement = null;
        int max_conv_ID = 0;
        try {

            statement = conn.prepareStatement("select MAX(MESSAGE_ID) from APP.MESSAGE WHERE CONVERSATION_ID=? AND CONVERSATION_INITIATOR=? GROUP BY CONVERSATION_ID,CONVERSATION_INITIATOR ");
            statement.setInt(1, conv.getConversation_id());
            statement.setString(2, conv.getConversation_initiator().getUsername());
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            if (resultSet.next()) {
                max_conv_ID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max_conv_ID;


    }

    public synchronized boolean addNewConv(Conversation conversation) {
        try {

            String template = "INSERT INTO APP.CONVERSATION (CONVERSATION_ID,CONVERSATION_INITIATOR,STARTED_DATE,CONV_TITLE,NEW_RECEIVED_MESSAGES) values (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setInt(1, conversation.getConversation_id());
            stmt.setString(2, conversation.getConversation_initiator().getUsername());
            stmt.setDate(3, java.sql.Date.valueOf(conversation.getStarted_date().toLocalDate()));
            stmt.setString(4, conversation.getTitle());
            if (conversation.getUnseenMessage()) {
                stmt.setString(5, "Y");
            } else {
                stmt.setString(5, "N");
            }
            stmt.executeUpdate();

            System.out.println("A Conv has stored.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;
        }

    }

    public synchronized boolean addChatPartnersToChatTable(int conversation_id, String conversation_initiator, String partner) {
        try {

            String template = "INSERT INTO APP.CHAT (CONVERSATION_ID,CONVERSATION_INITIATOR,PARTNER) values (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setInt(1, conversation_id);
            stmt.setString(2, conversation_initiator);
            stmt.setString(3, partner);

            stmt.executeUpdate();

            System.out.println("Chat table is updated with chat partners.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR8: " + ex.getMessage());
            return false;
        }

    }

    public synchronized boolean addAMessage(Message msg) {
        try {

            String template = "INSERT INTO APP.MESSAGE (CONVERSATION_ID,CONVERSATION_INITIATOR,MESSAGE_ID,MESSAGE_CREATOR,TIME,CONTENT,MSG_TYPE,MSG_STATUS ) values (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setInt(1, msg.getConversation_id());
            stmt.setString(2, msg.getConversation_initiator().getUsername());
            stmt.setInt(3, msg.getMessage_id());
            stmt.setString(4, msg.getMsg_creator());
            stmt.setDate(5, java.sql.Date.valueOf(msg.getSent_time().toLocalDate()));
            stmt.setString(6, msg.getContent());
            if (msg.getSent_received() == "Sent") {
                stmt.setString(7, "S");
            } else {
                stmt.setString(7, "R");
            }
            if (msg.getStatus() == "Delivered") {
                stmt.setString(8, "D");
            } else if (msg.getStatus() == "NotDelivered") {
                stmt.setString(8, "N");
            } else if (msg.getStatus() == "Seen") {
                stmt.setString(8, "S");
            } else {
                stmt.setString(8, "U");
            }
            stmt.executeUpdate();

            System.out.println("Message table is updated with new message.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR8: " + ex.getMessage());
            return false;
        }

    }
}




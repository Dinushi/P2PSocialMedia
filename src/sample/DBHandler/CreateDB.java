package sample.DBHandler;

import sample.Model.AbstractPeer;
import sample.Model.DiscoverdPeer;

import java.sql.*;
import java.sql.Blob;

public class CreateDB{
        private Connection conn= null;
        private Statement stmt =null;

        public CreateDB(){
            try {
                final String DB_URL = "jdbc:derby:MyDB;create=true";
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
                conn =
                        DriverManager.getConnection(DB_URL);
                System.out.println("Database Connected");
                this.stmt = conn.createStatement();
            }catch(Exception e){
                System.out.println("Error Connecting database");
                System.out.println(e.getMessage());
            }

            create_table_PEER();
            create_table_POST();
            build_table_REPLY();
            build_table_CONVERSATION();
            build_table_CHAT();
            build_table_MESSAGE();
        }
        private void create_table_PEER(){
            try {

                // Create the table.
                stmt.execute("CREATE TABLE PEER " +
                        "( USERNAME VARCHAR(30) PRIMARY KEY NOT NULL, " +
                        "  IP VARCHAR(20) NOT NULL," +
                        "  PORT INT NOT NULL," +
                        "  FULLNAME VARCHAR(100)," +
                        "  STATUS VARCHAR(100)," +
                        "  BIRTHDAY DATE ," +
                        "  GENDER VARCHAR(1)," +
                        //PROF+PIC
                        "  HOMETOWN VARCHAR(100) )");

                System.out.println("Peer table created.");
            } catch (SQLException ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }

    }
    public void create_table_POST() {
        try {
            // Create the table.
            stmt.execute("CREATE TABLE POST " +
                    "( USERNAME VARCHAR(30) NOT NULL REFERENCES PEER(USERNAME), " +
                    "  POST_ID INT NOT NULL," +
                    "  CONTENT VARCHAR(500)," +
                    "  CREATED_DATE DATE ," +
                    "  IMAGE BLOB(16M)," +
                    "  CONSTRAINT PK_Post PRIMARY KEY (USERNAME,POST_ID))");

            System.out.println("POST table created.");
        } catch (SQLException ex) {
            System.out.println("ERRORPost: " + ex.getMessage());
        }
    }
    public void build_table_REPLY() {
        try {
            // Create the table.
            stmt.execute("CREATE TABLE REPLY " +
                    "( USERNAME VARCHAR(30) NOT NULL REFERENCES PEER(USERNAME), " +
                    "  POST_ID INT  NOT NULL REFERENCES POST(POST_ID)," +
                    "  REPLY_ID INT NOT NULL ," +
                    "  CONTENT VARCHAR(300)," +
                    "  CREATED_DATE DATE,"+
                    "  CONSTRAINT PK_reply PRIMARY KEY (USERNAME,POST_ID,REPLY_ID))");

            System.out.println("REPLY table created.");
        } catch (SQLException ex) {
            System.out.println("ERRORReply: " + ex.getMessage());
        }
    }
    public void build_table_CONVERSATION() {
        try {

            // Create the table.
            stmt.execute("CREATE TABLE CONVERSATION " +
                    "( CONVERSATION_ID INT PRIMARY KEY , " +
                    "  STARTED_DATE DATE )");

            System.out.println("CONVERSATION table created.");
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    public void build_table_CHAT() {
        try {
            // Create the table.
            stmt.execute("CREATE TABLE CHAT " +
                    "( CONVERSATION_ID INT NOT  NULL REFERENCES CONVERSATION(CONVERSATION_ID), " +
                    "PARTNER VARCHAR(30) NOT NULL REFERENCES PEER(USERNAME), " +
                    " CONSTRAINT PK_chat PRIMARY KEY (CONVERSATION_ID,PARTNER))");

            System.out.println("CHAT table created.");
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    public void build_table_MESSAGE() {
        try {
            // Create the table.
            stmt.execute("CREATE TABLE MESSAGE " +
                    "( CONVERSATION_ID INT NOT  NULL REFERENCES CONVERSATION(CONVERSATION_ID), " +
                    "  MESSAGE_ID INT NOT NULL ," +
                    "  TIME DATE ," +
                    "  CONTENT VARCHAR(300)," +
                    "  MSG_TYPE VARCHAR(1)," +
                    "  DELIVERD_STATUS VARCHAR(1)," +
                    "  CONSTRAINT PK_message PRIMARY KEY (CONVERSATION_ID,MESSAGE_ID ))");
            System.out.println("MESSAGE table created.");
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }


}



package sample.DBHandler;

import sample.Model.AbstractPeer;
import sample.Model.Peer;

import java.sql.*;

public class DatabaseHandler {

        private static final String DB_URL="jdbc:derby:C:/Users/Dinushi Ranagalage/IdeaProjects/P2PSocialMedia/PeerDB";
        private static Connection conn= null;
        private static Statement stmt =null;
        public DatabaseHandler(){
            createConnection();
            //setUpUserDataTable();
            //check();

        }
        private void createConnection(){
            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Database connected");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void check(){
            try {
                stmt=conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT USERNAME FROM USER_DATA");
                while (rs.next()){
                    //this.user_id = rs.getInt("USER_ID")+1;
                    System.out.println("new user_id "+rs.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        private void setUpUserDataTable(){
            String table_name="USER_DATA";
            try{
                stmt=conn.createStatement();
                DatabaseMetaData dbm=conn.getMetaData();
                ResultSet tables=dbm.getTables(null,null, table_name.toUpperCase(),null);
                // if(tables.next()){
                // System.out.println("Table"+table_name+"already Exist");
                //}else {
                System.out.println("No table exist Exist");
                // Create the table.
                //stmt.execute("CREATE TABLE USER_DATA (USERNAME VARCHAR(15) NOT NULL PRIMARY KEY, PASSWORD VARCHAR(25),NAME VARCHAR(100) NOT NULL,BIRTHDAY DATE,HOMETOWN VARCHAR(100),GENDER CHAR(1))");
                //stmt.execute("CREATE TABLE USER_DATA (USERNAME VARCHAR(15) NOT NULL PRIMARY KEY, PASSWORD VARCHAR(25))");

                // Insert row #1.
                stmt.execute("INSERT INTO USER_DATA VALUES ( " +
                        "'pasindu3', " +
                        "'BOOO')");
                // }
            } catch (SQLException e) {
                e.printStackTrace();
            }
/*

             stmt.execute("CREATE TABLE "+table_name+"("
                        +" username varchar(100) primary key,\n"
                        +"password varchar(100),\n"
                        +")");
            }

*/

        }
    public boolean updateThisUserTable(AbstractPeer thispeer) {
        try {

            String template = "INSERT INTO THIS_USER (USERNAME,PASSWORD,IP,PORT) values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(template);
            stmt.setString(1,thispeer.getUsername());
            stmt.setString(2,"123");
            stmt.setString(3, String.valueOf(thispeer.getIp()));
            stmt.setInt(4,thispeer.getPort());
            stmt.executeUpdate();

            System.out.println("Statement has been successfully  executed.");
            return true;
        } catch (SQLException ex) {
            System.out.println("ERROR4: " + ex.getMessage());
            return false;
        }
    }
    }

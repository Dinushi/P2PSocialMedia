package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.CommunicationHandler.PeerConnection;
import sample.DBHandler.CreateDB;
import sample.DBHandler.DbHandler;
import sample.Model.Peer;
import sample.Model.Post;

import java.net.InetAddress;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
        primaryStage.setTitle("Login P2P social media");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

/*
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = FXMLLoader.load(getClass().getResource("View/Chats.fxml"));
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    */
    /*
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/AppHome.fxml"));
        primaryStage.setTitle("Login P2P social media");
        Scene scene=new Scene(root, 445, 565);
        //scene.getStylesheets().add(getClass().getResource("../CSS/Conv.css").toString());
        //primaryStage.setScene(scene);
        primaryStage.setScene(new Scene(root, 445, 565));
        primaryStage.show();
    }
    */

    public static void main(String[] args) throws Exception {

        launch(args);
        //CreateDB db=new CreateDB();

        //PeerConnection we=new PeerConnection();
        //we.createTheSocketListner();
        /*
        DbHandler db=new DbHandler();
        Peer p1=new Peer("chandrasiri12", InetAddress.getByName("Localhost"),3784,true);
        p1.setStatus("mommmmy");
        p1.setGender("F");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        p1.setBday(sdf.parse("1957-07-26"));
        p1.setHometown("Akuressa");
        p1.setFullname("R chanadraasiri ");


        db.addAnewPeer(p1);
         */
        //DbHandler db=new DbHandler();
        //Post p1=new Post("chandrasiri12", "a country like this will never found in any planet.");
        //p1.addToDb();
        //Post p2=new Post("Dinushi123", "People are sooo coool here.I like it...gilr.");
        //p2.addToDb();
        //Post p3=new Post("Pasindu1991", "a country like this will never found in any planet.");
        //p3.addToDb();
    }

}

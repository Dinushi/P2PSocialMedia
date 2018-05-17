package sample.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.CommunicationHandler.PeerConnection;
import sample.EventHandler.HeartBeatHandler;
import sample.EventHandler.NewPeerListner;
import sample.Model.Peer;
import sample.Model.ThisPeer;


import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static sample.Controller.Validator.thisPeer;

/**
 * Created by Pasidu Chinthiya on 1/31/2018.
 */
public class LoginController {

    // Authenticate auth;
    // The reference of inputText will be injected by the FXML loader
    @FXML
    private JFXTextField userName;

    @FXML
    private JFXButton btn_login;

    // The reference of outputText will be injected by the FXML loader
    @FXML
    private JFXPasswordField password;
    PeerConnection pc;


    public void pressLogin(ActionEvent event){


        Window owner = btn_login.getScene().getWindow();

        Validator v=new Validator();



        int result = v.validateUser(userName.getText(), password.getText());

        if (result==0){

            if(!PeerConnection.initialLogin){
                pc=PeerConnection.getPeerConnection();
                pc.createTheSocketListner();
                if(thisPeer==null){
                    thisPeer= Peer.retrieveAPeer(userName.getText());
                    thisPeer.setOnlineStatus(true);
                }

                String Strlast_logout_time=readTheUserHistory();//The last log out time of the user is read
                long millis=Long.parseLong(Strlast_logout_time);
                Instant instant = Instant.ofEpochMilli(millis);
                LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                System.out.println("last_log_out_time"+date);
                Validator.last_logOuttime= date;
            }
            new HeartBeatHandler().start();

            //NewPeerListner.sendJoinRequestToDiscoverdPeersFromBs();
            //now only the port is specified to create a peer connection


            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Login Successful!",
                    "Welcome " + userName.getText());

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/sample/View/AppHome.fxml"));
                Stage stage = new Stage();
                stage.setTitle("PeerNet");
                Scene scene=new Scene(root, 747, 601);
                stage.setScene(scene);
                stage.setResizable(false);
                scene.getStylesheets().add(getClass().getResource("/sample/CSS/Home.css").toString());

                stage.setOnHiding(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                if(HomeController.notALoginOut){
                                    System.out.println("closing the Home page");
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Confirmation Dialog");
                                    alert.setHeaderText("Log out");
                                    alert.setContentText("Do you want to log out?");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK ) {
                                        try {
                                        /*
                                        System.out.println("Store history of user");
                                        BufferedWriter writer= new BufferedWriter(new FileWriter("ThisUser.txt", true));
                                        LocalDateTime log_out_time=LocalDateTime.now();
                                        writer.write(String.valueOf(log_out_time));
                                        writer.newLine();
                                        System.out.println("log out time"+log_out_time);
                                        */

                                            BufferedWriter writer = new BufferedWriter(new FileWriter("History.txt"));
                                            long log_out_time=System.currentTimeMillis();


                                            System.out.println("storing the message created date"+String.valueOf(log_out_time));
                                            writer.write(String.valueOf(log_out_time));

                                            //Timestamp timestamp = Timestamp.valueOf(log_out_time);
                                            //System.out.println("storing the message created date"+timestamp);
                                            //writer.write(String.valueOf(timestamp));
                                            System.out.print("History of user is stored");
                                            writer.close();

                                            System.exit(0);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    //also send these changes to a all connected Peers
                                    //} else {
                                    // ... user chose CANCEL or closed the dialog

                                }
                                }

                        });
                    }
                });
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if(result==1){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Login Error!",
                    "Please enter the correct password");
        }else if(result==4){
            AlertHelper.showAlert(Alert.AlertType.WARNING, owner, "Require Registration",
                    "You have to Register First!");
        } else{
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Login Error!",
                    "Username is Invalid");
        }

    }

    public void pressCancel(ActionEvent event){
        System.out.println("Asked to cancel");
        this.userName.setText("");
        this.password.setText("");
    }

    public void pressRegister(ActionEvent event) throws Exception{
        System.out.println("New User is Ready to Register");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/View/Register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Register Form");
            Scene scene=new Scene(root, 750, 550);
            //stage.setScene(new Scene(root, 400, 600));
            stage.setScene(scene);
            stage.setResizable(false);
            scene.getStylesheets().add(getClass().getResource("/sample/CSS/Register.css").toString());
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String readTheUserHistory(){

        String last_logged_time;
        // The name of the file to open.
        String fileName = "History.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
           line = bufferedReader.readLine();
           last_logged_time=line.replaceAll("[\n\r]", "");
           bufferedReader.close();
           fileReader.close();
           return last_logged_time;
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
            return "";
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
            return "";
        }

    }

}

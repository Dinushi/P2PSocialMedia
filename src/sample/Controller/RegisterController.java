package sample.Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.CommunicationHandler.ClientConnection;
import sample.CommunicationHandler.PeerConnection;
import sample.DBHandler.CreateDB;
import sample.EventHandler.NewPeerListner;
import sample.Model.Owner;
import sample.Model.Peer;
import sample.Model.ThisPeer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.sql.Date;

import static sample.Controller.Validator.thisPeer;

public class RegisterController {

    @FXML
    private TextField name;

    @FXML
    private TextField userName;

    @FXML
    private TextField hometown;

    @FXML
    private RadioButton check_male;

    @FXML
    private RadioButton check_female;

    @FXML
    private DatePicker birthday;

    @FXML
    private TextField password;

    @FXML
    private Button btn_submit;

    @FXML
    private TextField re_password;



    //private Client client;######################
    private ClientConnection client;//??????????????
    Window owner;

    public void pressSubmit(ActionEvent event) {
        this.owner = btn_submit.getScene().getWindow();

        //this.validateUsername();
        //this.validatePassword();
        //validate the other data input by user

        //sent username,ip,port to bootstrap server
        this.connectToServer();

        //create the database
        CreateDB dbCreator=new CreateDB();
        //create the thisUser
        this.createThisUser();

        PeerConnection peerCon=PeerConnection.getPeerConnection();
        peerCon.createTheSocketListner();//a listen socket will be created to listen on requests.
        PeerConnection.initialLogin=true;
        //assign this work to a thrad to allow re transmissions and continue login page dispaly quickly
        NewPeerListner.sendJoinRequestToDiscoverdPeersFromBs();//peer connections are set with discoverd peers


        try {
            createTheUserCredentials();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sendConfirmation();
        this.showLoginPage(event);


        //from contoller level validate for password for minimum 8 characters
        //this.auth=new Authenticate(userName.getText(),password.getText());
        //ConnectDb connectDb=new ConnectDb();
    }

    private void validatePassword() {
        if (password != re_password) {
            //show A dialog with msg password mismatch
        } else {
            //if()
            //check whether password contain maximum 8 characters with letters,symbols and numbers
        }
    }

    public void validateUsername() {
    }

    private void connectToServer() {
        this.client=new ClientConnection(userName.getText());

    }

    private void createThisUser() {
        Owner.myIP =client.getLocal_address();
        Owner.myUsername=userName.getText();
        Owner.myPort=client.getLocal_port();
        //validator has this variable
        thisPeer=new Peer(Owner.myUsername,Owner.myIP,Owner.myPort,false);

        thisPeer.setFullname(name.getText());//but you have to validate before using
        thisPeer.setHometown(hometown.getText());
        thisPeer.setBday(Date.valueOf(birthday.getValue()));
        if(check_male.isSelected())
            thisPeer.setGender("M");
        else
            thisPeer.setGender("F");
        boolean result=thisPeer.insertToDb();
        if(result){
            System.out.println("This user has been successfully stored.");
        }else{
            System.out.println("This user failed to store.");
        }

    }
    private void createTheUserCredentials() throws IOException{

        BufferedWriter writer = new BufferedWriter(new FileWriter("ThisUser.txt"));
        writer.write(userName.getText());
        writer.newLine();
        writer.write(String.valueOf(client.getLocal_address().toString().split("/")[1]));
        writer.newLine();
        writer.write(String.valueOf(client.getLocal_port()));
        writer.newLine();
        writer.write(String.valueOf(password.getText()));
        writer.close();
        System.out.print("User Credentials has added in the file");

    }
    private void sendConfirmation(){

        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, this.owner, "Successfully registered in the Bootstrap Server!",
                "Now you can Login");

    }
    private void showLoginPage(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login P2P social media");
            Scene scene=new Scene(root, 400, 300);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../CSS/Login.css").toString());
            stage.show();
            // Hide this current window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //private void createClientConnection() {
      //  ClientConnection connection = new ClientConnection("127.0.0.1", 55555, (Serializable data) -> {
        //    Platform.runLater(() -> {
          //      userName.appendText(data.toString());
         //   });
        //});

   // }
}


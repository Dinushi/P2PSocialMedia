package sample.Controller;

import javafx.application.Platform;
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

public class RegisterController {

    @FXML
    private TextField name;

    @FXML
    private TextField userName;

    @FXML
    private TextField hometown;

    @FXML
    private RadioButton male;

    @FXML
    private RadioButton female;

    @FXML
    private DatePicker birthday;

    @FXML
    private TextField password;

    @FXML
    private Button btn_submit;

    @FXML
    private TextField re_password;
    public Peer thisPeer;
    //private Client client;######################
    private ClientConnection client;//??????????????
    Window owner;

    public void pressSubmit(ActionEvent event) {
        this.owner = btn_submit.getScene().getWindow();
        System.out.println("Hello you are welcomed" + userName.getText());
        System.out.println("Hello you are welcomed" + password.getText());
        //this.validateUsername();
        //this.validatePassword();
        //validate the other data input by user
        //sent username,ip,port to bootstrap server
        this.connectToServer();
        //create the database
        //CreateDB dbCreator=new CreateDB();
        this.createThisUser();
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
        //thisPeer=new Peer();tikak hithala balala karanam
        ThisPeer me=new ThisPeer(userName.getText(),this.client.getLocal_address(),this.client.getLocal_port(),password.getText());
        NewPeerListner.sendJoinRequestToDiscoverdPeer();//peer connections are set with discoverd peers
        PeerConnection peerCon=new PeerConnection();
        peerCon.createTheSocketListner();//a listen socket will be created to listen on requests.
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
        System.out.print("user Credentials has added in the file");


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


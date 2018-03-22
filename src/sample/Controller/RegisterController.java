package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import sample.CommunicationHandler.ClientConnection;
import sample.Model.ThisPeer;

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
    private TextField re_password;
    //private Client client;######################
    private ClientConnection client;//??????????????

    public void pressSubmit(ActionEvent event) {
        System.out.println("Hello you are welcomed" + userName.getText());
        System.out.println("Hello you are welcomed" + password.getText());
        //this.validateUsername();
        //this.validatePassword();
        //validate the other data input by user
        //sent username,ip,port to bootstrap server
        this.connectToServer();
        this.createThisUser();
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
        ThisPeer me=new ThisPeer(userName.getText(),this.client.getLocal_address(),this.client.getLocal_port(),password.getText());

    }
    private void showLoginPage(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login P2P social media");
            stage.setScene(new Scene(root, 400, 300));
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


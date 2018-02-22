package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import sample.CommunicationHandler.ClientConnection;
import sample.Model.ThisPeer;

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
        //this.client=new Client(userName.getText());###############################
        this.client=new ClientConnection(userName.getText());
    }//when modify add the clientConnection class ofcommunication handler instead

    private void createThisUser() {
        ThisPeer me=new ThisPeer(userName.getText(),this.client.getLocal_address(),this.client.getLocal_port(),password.getText());

    }
    //private void createClientConnection() {
      //  ClientConnection connection = new ClientConnection("127.0.0.1", 55555, (Serializable data) -> {
        //    Platform.runLater(() -> {
          //      userName.appendText(data.toString());
         //   });
        //});

   // }
}


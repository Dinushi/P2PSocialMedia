package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import sample.CommunicationHandler.PeerConnection;
import sample.DBHandler.DatabaseHandler;


import java.io.IOException;

/**
 * Created by Pasidu Chinthiya on 1/31/2018.
 */
public class LoginController {

    // Authenticate auth;
    // The reference of inputText will be injected by the FXML loader
    @FXML
    private TextField userName;

    // The reference of outputText will be injected by the FXML loader
    @FXML
    private TextField password;


    public void pressLogin(ActionEvent event){
        PeerConnection pc=new PeerConnection();
        //System.out.println("Socket is listning");
        //pc.createTheSocketListner();
        System.out.println("Socket is sending");

        pc.sendViaSocket();
        System.out.println("Hello you are welcomed"+userName.getText());
        System.out.println("Hello you are welcomed"+password.getText());
        //from contoller level validate for password for minimum 8 characters
        //this.auth=new Authenticate(userName.getText(),password.getText());
        //DatabaseHandler DBhandler=new DatabaseHandler();

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
            Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Register Form");
            stage.setScene(new Scene(root, 400, 600));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

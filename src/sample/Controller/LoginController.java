package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import sample.CommunicationHandler.PeerConnection;
import sample.DBHandler.DatabaseHandler;
import sample.Model.ThisPeer;


import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Pasidu Chinthiya on 1/31/2018.
 */
public class LoginController {


    // Authenticate auth;
    // The reference of inputText will be injected by the FXML loader
    @FXML
    private TextField userName;

    @FXML
    private Button btn_login;

    // The reference of outputText will be injected by the FXML loader
    @FXML
    private TextField password;
    PeerConnection pc;

    public void pressLogin(ActionEvent event){

        Window owner = btn_login.getScene().getWindow();
        Validator v=new Validator();
        int result=v.validateUser(userName.getText(),password.getText());

        if (result==0){
            //better to make this peer singleton if needed.
            ThisPeer me=new ThisPeer(userName.getText(),v.getMyIp(),v.getMyPort(),password.getText());
            pc=new PeerConnection();

            System.out.println("Socket is listning");
            pc.createTheSocketListner(v.getMyPort());
            //now only the port is specified to create a peer connection

            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Login Successful!",
                    "Welcome " + userName.getText());

            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/AppHome.fxml"));
                Stage stage = new Stage();
                stage.setTitle("PeerNet");
                stage.setScene(new Scene(root, 577, 602));
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if(result==1){
            AlertHelper.showAlert(Alert.AlertType.ERROR, owner, "Login Error!",
                    "Please enter the correct password");

        }else{
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
            Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Register Form");
            Scene scene=new Scene(root, 750, 550);
            //stage.setScene(new Scene(root, 400, 600));
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("Register.css").toString());
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class LoginConfirmationController {



    //showLoginPage
    public void pressLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/View/Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login P2P social media");
            Scene scene=new Scene(root, 547, 372);
            stage.setScene(scene);
            stage.setResizable(false);
            scene.getStylesheets().add(getClass().getResource("/sample/CSS/Login.css").toString());
            stage.show();
            // Hide this current window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

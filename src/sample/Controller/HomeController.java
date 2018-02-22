package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Model.Post;

import java.io.IOException;

public class HomeController{
    @FXML
    private TextField txt_created_date;
    @FXML
    private TextField txt_content;

    public void showNewPost(Post post){
        System.out.println("New User is Ready to Register");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/Home.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Home Form");
            stage.setScene(new Scene(root, 400, 600));
            stage.show();
            this.txt_content.setText(post.getContent());
            this.txt_created_date.setText(post.getDate_created().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

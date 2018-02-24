package sample.Controller;

import javafx.application.Platform;
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
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        //this method is called by a normal thread.But we can handle UI compoents only using Application thread.
        //so this tunlater allows the normal thread to run on application thraead for a while and do the task
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String content = post.getContent();
                    String date = post.getDate_created().toString();
                    Parent root = FXMLLoader.load(getClass().getResource("../View/Home.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Home Form");
                    stage.setScene(new Scene(root, 400, 600));
                    stage.show();
                    txt_content.setText(content);
                    txt_created_date.setText(date);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

}

package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class ConversationController {


    ListView<String> list = new ListView<String>();
    ObservableList<String> items = FXCollections.observableArrayList (
            "Single", "Double", "Suite", "Family App");
    //list.setItems(items);



}

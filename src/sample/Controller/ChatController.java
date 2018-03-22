package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Model.Conversation;

import java.io.IOException;
import java.util.ArrayList;

public class ChatController {

    @FXML
    private TabPane tabPane;

    public void initialize() {

        Tab tab = new Tab("All Conversations");
        // Rectangle rect = new Rectangle(200, 200, Color.RED);
        //VBox v=new VBox();
        ListView<Pane> list = new ListView<Pane>();
        ObservableList<Pane> panes = FXCollections.observableArrayList();

        for (int i = 0; i < 5; i++) {
            // StackPane p1= new StackPane();
            //p1.setAlignment(Pos.CENTER);

            FlowPane p1 = new FlowPane();
            p1.setVgap(8);
            p1.setHgap(4);
            p1.setPrefWrapLength(300);
            Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));
            ImageView img = new ImageView(image);
            img.setFitHeight(50);
            img.setFitWidth(50);
            img.setPreserveRatio(true);

            Label label3 = new Label("Dinushi1234", img);
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 15));


            Label label4 = new Label("Please come to the University tomorrow.I want to talk to to you");
            label4.setFont(Font.font("Arial", 12));
            label4.setWrapText(true);
            Button b1 = new Button("View");

            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    int numTabs = tabPane.getTabs().size();
                    Tab tab = new Tab("Tab " + (numTabs + 1));
                    tabPane.getTabs().add(tab);
                }
            });
            b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p1.getChildren().addAll(label3, label4, b1);

            panes.add(p1);
            //v.getChildren().setAll(p1);
            //tab.setContent(v);
        }
        list.setItems(panes);
        tab.setContent(list);
        tabPane.getTabs().add(tab);

        //A label with the text element
        //Label label2 = new Label("Dinushi1234");
        //A label with the text element and graphical icon

        //Label label1 = new Label("Dinushi1234");
        //Label label3 = new Label("Dinushi1234", img);
        //label3.setMinWidth(50);
        //label3.setMinHeight(50);
        //p1.getChildren().addAll(label1,label3 );
        //v.getChildren().setAll(p1);
        //tab.setContent(v);

        //ListView<Pane> list = new ListView<Pane>();
        //ObservableList<Pane> panes = FXCollections.observableArrayList (
        // p1,new Pane());

        ObservableList<String> items = FXCollections.observableArrayList(
                "Single", "Double", "Suite", "Family App");
        //list.setItems(panes);

        //ScrollPane s1 = new ScrollPane();
        //s1.setPrefSize(120, 120);
        //s1.setContent(rect);
        //tab.setContent(list);
        // tabPane.getTabs().add(tab);

    }


    @FXML
    private void addTab() {
        int numTabs = tabPane.getTabs().size();
        Tab tab = new Tab("Tab " + (numTabs + 1));
        tabPane.getTabs().add(tab);
    }

    @FXML
    private void listTabs() {
        tabPane.getTabs().forEach(tab -> System.out.println(tab.getText()));
        System.out.println();
    }
    public void createNewConversation(ActionEvent event){
        //peersla prompt karaan page ekak pennana
        //methana mulu page ekama generate karanan
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/addPartner.fxml"));
            Stage stage2 = new Stage();
            stage2.setTitle("New Conversation");
            stage2.setScene(new Scene(root, 282, 466));
            stage2.show();
            Conversation conv=new Conversation();


            for (String partner : AddPartnerController.selectedPartners) {
                conv.selectPeer(partner);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


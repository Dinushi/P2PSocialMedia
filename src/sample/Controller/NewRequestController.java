package sample.Controller;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.EventHandler.NewPeerListner;
import sample.Model.Conversation;
import sample.Model.DiscoverdPeer;
import sample.Model.Peer;
import sample.Model.Post;

import java.io.IOException;
import java.util.ArrayList;

public class NewRequestController {
    @FXML
    private Pane request_pane;

    @FXML
    private Button btn_ok;

    @FXML
    private Button btn_cancel;


    ArrayList<Peer> selectedPeers;
    ArrayList<Peer> rejectedPeers;

    public void initialize() {
        ArrayList<Peer> allRequests = NewPeerListner.getRequestedPeers();
        System.out.println("Requsts"+allRequests);
        System.out.println("At addpartnerC:" + allRequests);

        selectedPeers = new ArrayList<>();
        rejectedPeers = new ArrayList<>();

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(238, 366);

        ListView<Pane> list = new ListView<Pane>();
        ObservableList<Pane> panes = FXCollections.observableArrayList();

        Tab tab = new Tab("Recent Requests");

        for (int i = 0; i < allRequests.size(); i++) {

            FlowPane p1 = new FlowPane();
            p1.setVgap(6);
            p1.setHgap(60);
            p1.setPrefWrapLength(200);
            Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
            ImageView img = new ImageView(image);
            img.setFitHeight(35);
            img.setFitWidth(35);
            img.setPreserveRatio(true);

            Label label3 = new Label(allRequests.get(i).getUsername());
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 12));
            Button b1 = new Button("Confirm");
            b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            b1.setId(String.valueOf(i));//set the user name as the id of the button


            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Peer choosedPeer = allRequests.get(Integer.parseInt(b1.getId()));
                    if (b1.getText() == "Confirm") {
                        ((Control) e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #1e90ff;");
                        b1.setText("Confirmed");
                        selectedPeers.add(choosedPeer);
                    } else {
                        ((Control) e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #a9a9a9;");
                        b1.setText("Confirm");
                        selectedPeers.remove(choosedPeer);
                    }
                }
            });
            Button b2 = new Button("Reject");
            b2.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            //b2.setId(String.valueOf(allRequests.get(i).getUsername()));//set the user name as the id of the button
            b2.setId(String.valueOf("R"+i));

            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    Peer choosedPeer = allRequests.get(Integer.parseInt(b2.getId().substring(1)));
                    if (b1.getText() == "Reject") {
                        ((Control) e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #1e90ff;");
                        b2.setText("Rejected");
                        rejectedPeers.add(choosedPeer);
                    } else {
                        ((Control) e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #a9a9a9;");
                        b2.setText("Reject");
                        rejectedPeers.remove(choosedPeer);
                    }
                }
            });

            b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p1.getChildren().addAll(label3, b1,b2);

            panes.add(p1);
        }
        list.setItems(panes);
        s1.setContent(list);
        request_pane.getChildren().add(s1);


    }

    private class Worker extends Thread{
        public void run(){
            NewPeerListner.sendTheConfirmation(selectedPeers,rejectedPeers);
        }

    }
    //try to use a anoynemous inner cass to assign this task to a thrad
    public void sendUserChoice(ActionEvent event){
        ((Node) (event.getSource())).getScene().getWindow().hide();
        new Worker().start();
    }
    //public void sendUserChoice(ActionEvent e){
        //allow a sepearte thraead to do this tas
        //NewPeerListner.sendTheConfirmation(selectedPeers,rejectedPeers);
    //}
    public void pressCancel(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/peerRequests.fxml"));
            Stage stage = new Stage();
            stage.setTitle("New Peer Requests");
            stage.setScene(new Scene(root, 369.0, 465.0));
            stage.show();

        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }

}

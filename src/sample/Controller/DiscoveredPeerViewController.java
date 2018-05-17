package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import sample.DBHandler.DbHandler;
import sample.EventHandler.NewPeerListner;
import sample.Model.DiscoverdPeer;

import java.io.IOException;
import java.util.ArrayList;

public class DiscoveredPeerViewController {
    public static DiscoveredPeerViewController discoveredPeerViewController;
    public static ArrayList<DiscoverdPeer> selectedDiscoverdPeers;
    public static ArrayList<DiscoverdPeer> allDiscoveredPeers;
    @FXML
    private Pane peerPane;

    @FXML
    private Pane paneTitle;
    @FXML
    private JFXButton btn_connect;
    public DiscoveredPeerViewController(){discoveredPeerViewController=this;}


    public void initialize() {
        DbHandler db=new DbHandler();
        allDiscoveredPeers= db.selectAllDiscoveredPeers();
        db.closeConnection();

        selectedDiscoverdPeers=new ArrayList<>();
        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(238, 366);

        ListView<Pane> list = new ListView<Pane>();
        ObservableList<Pane> panes = FXCollections.observableArrayList();

        for (int i = 0; i < allDiscoveredPeers.size(); i++) {

            FlowPane p1 = new FlowPane();
            p1.setVgap(6);
            p1.setHgap(60);
            p1.setPrefWrapLength(200);


            Label label3 = new Label(allDiscoveredPeers.get(i).getUsername());
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 12));
            Button b1 = new Button("Connect");
            b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            b1.setId(String.valueOf(i));//set the user name as the id of the button


            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    String peer_id=((Control)e.getSource()).getId();
                    int j=Integer.parseInt(peer_id);

                    if(b1.getText()=="Connect"){
                        ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #1e90ff;");
                        b1.setText("Selected");
                        selectedDiscoverdPeers.add(allDiscoveredPeers.get(j));
                    }else{
                        ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #a9a9a9;");
                        b1.setText("Connect");
                        selectedDiscoverdPeers.remove(allDiscoveredPeers.get(j));
                    }
                }
            });
            b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p1.getChildren().addAll(label3, b1);

            panes.add(p1);
        }
        list.setItems(panes);
        s1.setContent(list);
        peerPane.getChildren().add(s1);

    }

    public void pressOk(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        Window owner = btn_connect.getScene().getWindow();
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Following peers are selected to request new peer information",
                "With " + selectedDiscoverdPeers);

        NewPeerListner.sendJoinRequestToDiscoverdPeers(selectedDiscoverdPeers);
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    public void pressCancel(ActionEvent event){

    }
    public void pressDiscoverMore(ActionEvent event){
        System.out.println("Discoverd Peers are opening");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        if (SelectPeers.selectPeers== null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/sample/View/SelectPeers.fxml"));
                Stage stage = new Stage();
                stage.setTitle("All Your Peers");
                stage.setScene(new Scene(root, 323.0, 527.0));

                stage.setOnHiding(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("Closing select Peer UI");
                                SelectPeers.selectPeers=null;

                            }
                        });
                    }
                });
                stage.show();
                // Hide this current window (if this is what you want)
                //((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

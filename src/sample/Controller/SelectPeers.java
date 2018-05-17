package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.DBHandler.DbHandler;
import sample.EventHandler.NewPeerListner;
import sample.Model.Peer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SelectPeers  {
    public static SelectPeers selectPeers;
        public static ArrayList<Peer> selectedPeers;
        public static ArrayList<Peer> allKnownPeers;
        @FXML
        private Pane peerPane;

        @FXML
        private Pane paneTitle;
        @FXML
        private JFXButton btn_OK;
        public SelectPeers(){selectPeers=this;}


        public void initialize() {
            DbHandler db=new DbHandler();
            allKnownPeers = db.getAllPeers("T");
            db.closeConnection();

            selectedPeers=new ArrayList<>();
            ScrollPane s1 = new ScrollPane();
            s1.setPrefSize(258, 359);

            ListView<Pane> list = new ListView<Pane>();
            ObservableList<Pane> panes = FXCollections.observableArrayList();

            for (int i = 0; i < allKnownPeers.size(); i++) {

                FlowPane p1 = new FlowPane();
                p1.setVgap(6);
                p1.setHgap(60);
                p1.setPrefWrapLength(200);
                ImageView img2;

                if(allKnownPeers.get(i).getProf_pic()!=null){
                    ByteArrayInputStream in2 = new ByteArrayInputStream(allKnownPeers.get(i).getProf_pic());
                    BufferedImage read2 = null;
                    try {
                        read2 = ImageIO.read(in2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Image image1 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                    img2 = new ImageView();
                    img2.setFitHeight(35);
                    img2.setFitWidth(35);
                    img2.setPreserveRatio(true);
                    img2.setImage(SwingFXUtils.toFXImage(read2, null));
                }else{
                    Image image = new Image(getClass().getResourceAsStream("default.png"));//modify code to get the image from database
                    img2 = new ImageView(image);
                    img2.setFitHeight(35);
                    img2.setFitWidth(35);
                    img2.setPreserveRatio(true);
                }

                Label label3 = new Label(allKnownPeers.get(i).getUsername(), img2);
                label3.setTextFill(Color.web("#0076a3"));
                label3.setFont(Font.font("Cambria", 12));
                Button b1 = new Button("Request");
                b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
                b1.setId(String.valueOf(i));//set the user name as the id of the button


                b1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        String peer_id=((Control)e.getSource()).getId();
                        int j=Integer.parseInt(peer_id);

                        if(b1.getText()=="Request"){
                            ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #1e90ff;");
                            b1.setText("Selected");
                            selectedPeers.add(allKnownPeers.get(j));
                        }else{
                            ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #a9a9a9;");
                            b1.setText("Request");
                            selectedPeers.remove(allKnownPeers.get(j));
                        }
                    }
                });
                b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
                p1.getChildren().addAll(b1,label3);

                panes.add(p1);
            }
            list.setItems(panes);
            s1.setContent(list);
            peerPane.getChildren().add(s1);

        }

        public void pressOk(ActionEvent event) {
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            Window owner = btn_OK.getScene().getWindow();
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Following peers are selected to request new peer information",
                    "With " + selectedPeers);

            NewPeerListner.sendRequestForMorePeerDetails(selectedPeers);
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
        public void pressCancel(ActionEvent event){

        }
    }



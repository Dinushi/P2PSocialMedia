package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.EventHandler.HeartBeatHandler;
import sample.Model.Peer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class OnlinePeerController {
    public static OnlinePeerController onlinePeerController;
    public static ArrayList<Peer> allPeers;
    @FXML
    private Pane peerListPane;


    public OnlinePeerController(){onlinePeerController=this;}

    public void initialize() {
        allPeers= HeartBeatHandler.allConnectedPeers;
        System.out.println("size of peers at heaer"+allPeers.size());

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(255, 331);
        s1.setStyle("-fx-background: #4DD0E1;");

        ListView<Pane> list = new ListView<Pane>();
        ObservableList<Pane> panes = FXCollections.observableArrayList();

        for (int i = 0; i < allPeers.size(); i++) {
            if(allPeers.get(i).getOnlineStatus()){
                FlowPane p1 = new FlowPane();
                p1.setVgap(6);
                p1.setHgap(60);
                p1.setPrefWrapLength(200);
                p1.setStyle("-fx-background: #B2EBF2;");
                ImageView img2;

                if(allPeers.get(i).getProf_pic()!=null){
                    ByteArrayInputStream in2 = new ByteArrayInputStream(allPeers.get(i).getProf_pic());
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

                Label label3 = new Label(allPeers.get(i).getUsername(), img2);
                label3.setTextFill(Color.web("#0076a3"));
                label3.setFont(Font.font("Cambria", 12));


                p1.getChildren().addAll(label3);

                panes.add(p1);
            }
        }
        list.setItems(panes);
        s1.setContent(list);
        peerListPane.getChildren().add(s1);
    }


}

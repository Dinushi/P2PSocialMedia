package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.stage.WindowEvent;
import sample.DBHandler.DbHandler;
import sample.EventHandler.NewPeerListner;
import sample.Model.Peer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class ConnectedPeerListController implements Initializable {
    @FXML
    private Pane peerListPane;
    public static ConnectedPeerListController connectedPeerListController;
    private ArrayList<Peer>  allPeers;

    public ConnectedPeerListController(){connectedPeerListController=this;}

    public void initialize(URL url, ResourceBundle rs) {
        DbHandler db=new DbHandler();
        allPeers = db.getAllPeers("T");
        db.closeConnection();

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(334, 460);

        ListView<Pane> list = new ListView<Pane>();
        ObservableList<Pane> panes = FXCollections.observableArrayList();

        for (int i = 0; i < allPeers.size(); i++) {

            FlowPane p1 = new FlowPane();
            p1.setVgap(6);
            p1.setHgap(60);
            p1.setPrefWrapLength(200);
            ImageView img;
            if(allPeers.get(i).getProf_pic()!=null){
                ByteArrayInputStream in2 = new ByteArrayInputStream(allPeers.get(i).getProf_pic());
                BufferedImage read = null;
                try {
                    read = ImageIO.read(in2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Image image1 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                img = new ImageView();
                img.setFitHeight(35);
                img.setFitWidth(35);
                img.setPreserveRatio(true);
                img.setImage(SwingFXUtils.toFXImage(read, null));
            }else{
                Image image = new Image(getClass().getResourceAsStream("default.png"));//modify code to get the image from database
                img = new ImageView(image);
                img.setFitHeight(35);
                img.setFitWidth(35);
                img.setPreserveRatio(true);
            }

            Label label3 = new Label(allPeers.get(i).getUsername(), img);
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 12));
            Button b1 = new Button("View");
            b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            b1.setId(String.valueOf(i));//set the user name as the id of the button


            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {

                        try {
                            int j =Integer.parseInt(b1.getId());
                            System.out.println("selected Peer id"+j);
                            Peer peer=allPeers.get(j);

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/PeerProfile.fxml"));

                            // Create a controller instance
                            PeerProfileController controller = new PeerProfileController(peer);
                            // Set it in the FXMLLoader
                            loader.setController(controller);
                            Parent root= loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Peer Profile");
                            stage.setScene(new Scene(root, 650.0, 574.0));
                            stage.show();
                            /*
                            Parent root = FXMLLoader.load(getClass().getResource("../View/PeerProfile.fxml"));
                            Stage stage = new Stage();
                            stage.setTitle("Peer Profile");
                            stage.setScene(new Scene(root, 650.0, 574.0));
                            stage.show();
                            */
                            /*
                            //UI eka initialize unata passe value eka use wenw witharak this is ok..But can't be used if it is referd in initialize()
                            FXMLLoader fxmlLoader=new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("../View/PeerProfile.fxml"));
                            fxmlLoader.load();

                            PeerProfileController peerProfileController=fxmlLoader.getController();
                            peerProfileController.setChoosedPeer(peer);

                            Parent p=fxmlLoader.getRoot();
                            Stage stage = new Stage();
                            stage.setTitle("Peer Profile");
                            stage.setScene(new Scene(p, 650.0, 574.0));
                            stage.showAndWait();
*/
                            /*
                            int j =Integer.parseInt(b1.getId());
                            System.out.println("selected Peer id"+j);
                            Peer p=allPeers.get(j);

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/PeerProfile.fxml"));

                            Parent root = (Parent)fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Peer Profile");
                            PeerProfileController controller = fxmlLoader.<PeerProfileController>getController();
                            controller.setChoosedPeer(p);
                            Scene scene = new Scene(root,650.0, 574.0);

                            stage.setScene(scene);

                            stage.show();
                            */

                            // Hide this current window (if this is what you want)
                            //((Node) (event.getSource())).getScene().getWindow().hide();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
            });
            b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p1.getChildren().addAll(label3, b1);

            panes.add(p1);
        }
        list.setItems(panes);
        s1.setContent(list);
        peerListPane.getChildren().add(s1);

    }

}

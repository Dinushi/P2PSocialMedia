package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Window;
import sample.Model.Conversation;

import java.io.IOException;
import java.util.ArrayList;

public class AddPartnerController {
    public static ArrayList<String> selectedPartners;
    @FXML
    private Pane peerPane;
    @FXML
    private Button btn_newChatOK;


    public void initialize() {
        ArrayList<String> allPeerUsernames=Conversation.selectAllPeerUsernames();
        System.out.println("At addpartnerC:"+allPeerUsernames);
        selectedPartners=new ArrayList<>();
        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(238, 366);

        ListView<Pane> list = new ListView<Pane>();
        ObservableList<Pane> panes = FXCollections.observableArrayList();

        Tab tab = new Tab("All Conversations");
    ;

        for (int i = 0; i < allPeerUsernames.size(); i++) {

            FlowPane p1 = new FlowPane();
            p1.setVgap(6);
            p1.setHgap(60);
            p1.setPrefWrapLength(200);
            Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
            ImageView img = new ImageView(image);
            img.setFitHeight(35);
            img.setFitWidth(35);
            img.setPreserveRatio(true);

            Label label3 = new Label(allPeerUsernames.get(i), img);
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 12));
            Button b1 = new Button("Select");
            b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            b1.setId(String.valueOf(allPeerUsernames.get(i)));//set the user name as the id of the button


            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    String partner=((Control)e.getSource()).getId();
                    if(b1.getText()=="Select"){
                        ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #1e90ff;");
                        b1.setText("Selected");
                        selectedPartners.add(partner);
                    }else{
                        ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #a9a9a9;");
                        b1.setText("Select");
                        selectedPartners.remove(partner);
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
    public void pressOk(ActionEvent event) throws IOException {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        Window owner = btn_newChatOK.getScene().getWindow();
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "New Conversation Created",
                "With " + selectedPartners);
        Parent root = FXMLLoader.load(getClass().getResource("../View/addPartner.fxml"));
        Stage stage2 = new Stage();
        stage2.setTitle("New Conversation");
        stage2.setScene(new Scene(root, 800, 600));
        stage2.show();

    }
    public void pressCancel(ActionEvent event){

    }
}

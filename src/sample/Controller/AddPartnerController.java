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
import javafx.stage.Window;
import sample.DBHandler.DbHandler;
import sample.EventHandler.ConversationHandler;
import sample.Model.Conversation;
import sample.Model.DiscoverdPeer;
import sample.Model.Owner;
import sample.Model.Peer;

import java.io.IOException;
import java.util.ArrayList;

public class AddPartnerController {
    public static ArrayList<String> selectedPartners;
    @FXML
    private Pane peerPane;

    @FXML
    private Pane paneTitle;
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
        //In here the conversation is created and send the conversation object to other peers
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        Window owner = btn_newChatOK.getScene().getWindow();
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "New Conversation Created",
                "With " + selectedPartners);
        Conversation conv=new Conversation();
        DbHandler db = new DbHandler();
        conv.setConversation_id(db.getMaxConvID()+1);
        conv.setConversation_initiator(Validator.thisPeer);
        conv.setUnseenMessages(false);

        for (String username : selectedPartners) {
            conv.selectPeer(username);
        }
        if(selectedPartners.size()==1){
            conv.setTitle(selectedPartners.get(0));
        }else{
            FlowPane f_pane=new FlowPane();
            Label label3 = new Label("Conversation Name");
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 12));

            TextField textField2 = new TextField ();
            textField2.setPromptText("Conversation Name");

            textField2.setPrefWidth(150);
            f_pane.getChildren().addAll(label3,textField2);


            Button b1 = new Button("OK");
            b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    String title = textField2.getText();
                    conv.setTitle(title);
                }
            });
            paneTitle.getChildren().addAll(f_pane,b1);
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        ChatController.chatController.createNewConversationTab(conv,true);

        //Parent root = FXMLLoader.load(getClass().getResource("../View/Chats.fxml"));
        //Stage stage = new Stage();
        //stage.setTitle("My Conversations");
        //stage.setScene(new Scene(root, 369.0, 465.0));

        ///stage.show();

    }
    public void pressCancel(ActionEvent event){

    }
}

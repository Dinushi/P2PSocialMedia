package sample.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import sample.EventHandler.HeartBeatHandler;
import sample.Model.Conversation;
import sample.Model.DiscoverdPeer;
import sample.Model.Owner;
import sample.Model.Peer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddPartnerController {
    public static ArrayList<Peer> selectedPartners;
    @FXML
    private Pane peerPane;

    @FXML
    private Pane paneTitle;
    @FXML
    private JFXButton btn_newChatOK;


    public void initialize() {
        DbHandler Db=new DbHandler();
        ArrayList<Peer> allPeers=Db.getAllPeers("T");
        Db.closeConnection();
        //ArrayList<Peer> allPeers= HeartBeatHandler.allConnectedPeers;//if want use this
        System.out.println("At addpartnerC:"+allPeers);
        selectedPartners=new ArrayList<>();
        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(238, 366);

        ListView<Pane> list = new ListView<Pane>();
        ObservableList<Pane> panes = FXCollections.observableArrayList();


        for (int i = 0; i < allPeers.size(); i++) {

                FlowPane p1 = new FlowPane();
                p1.setVgap(6);
                p1.setHgap(60);
                p1.setPrefWrapLength(200);
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
                Button b1 = new Button("Select");
                b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
                b1.setId(String.valueOf(i));//set the user name as the id of the button


                b1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        //String partner=((Control)e.getSource()).getId();
                        String partner=((Control)e.getSource()).getId();
                        int partner_id=Integer.parseInt(partner);
                        if(b1.getText()=="Select"){
                            ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #1e90ff;");
                            b1.setText("Selected");
                            selectedPartners.add(allPeers.get(partner_id));
                        }else{
                            ((Control)e.getSource()).setStyle("-fx-font: 09 arial; -fx-base:  #a9a9a9;");
                            b1.setText("Select");
                            selectedPartners.remove(allPeers.get(partner_id));
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
    //The selected peers are set and a conversation is created
    public void pressOk(ActionEvent event) throws IOException {
        //In here the conversation is created and send the conversation object to other peers
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        Window owner = btn_newChatOK.getScene().getWindow();
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information",
                "New Conversation Created" );
        Conversation conv=new Conversation();
        DbHandler db = new DbHandler();
        conv.setConversation_id(db.getMaxConvID()+1);
        conv.setConversation_initiator(Validator.thisPeer);
        conv.setUnseenMessages(false);

        for (Peer peer : selectedPartners) {
            conv.addPartner(peer);
            //conv.selectPeer(username);
        }
        if(selectedPartners.size()==1){
            conv.setTitle(selectedPartners.get(0).getUsername());
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
        System.out.println("Ceating a new Tab to the coversation");
        ChatController.chatController.createNewConversationTab(conv,true);


    }
    public void pressCancel(ActionEvent event){

    }
}

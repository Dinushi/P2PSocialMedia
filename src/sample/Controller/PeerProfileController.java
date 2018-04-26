package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.DBHandler.DbHandler;
import sample.Model.Owner;
import sample.Model.Peer;
import sample.Model.Post;
import sample.Model.Reply;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PeerProfileController implements Initializable {
    @FXML
    private Pane post_pane;
    @FXML
    private ImageView profPic;
    @FXML
    private Label lbl_myUername;
    @FXML
    private Label  lbl_status;
    @FXML
    private Label lbl_fullName;
    @FXML
    private Label lbl_birthday;
    @FXML
    private Label lbl_hometown;

    ArrayList<Post> allPosts;
    ListView<Pane> list ;
    ObservableList<Pane> panes;
    ScrollPane s1;
    Peer peer;
    DbHandler db;
    public PeerProfileController(Peer peer) {
        this.peer=peer;
    }
    public void initialize(URL url, ResourceBundle rb ) {
       // peer=find the way to get the selected peer to this place
        if(peer.getProf_pic()==null){
            showDefaultProfPic();

        }else{
            showProfPic(peer.getProf_pic());
        }
        this.lbl_status.setText(peer.getStatus());
        this.lbl_myUername.setText(peer.getUsername());
        this.lbl_fullName.setText(peer.getFullname());
        this.lbl_birthday.setText(peer.getBday().toString());
        this.lbl_hometown.setText(peer.getHometown());

        this.db=new DbHandler();
        allPosts=db.getPosts(true,peer.getUsername());

        displayPosts(allPosts);
        db.closeConnection();

    }

    private void displayPosts(ArrayList<Post> allPosts){

        System.out.println("At postsrC:"+allPosts);

        s1 = new ScrollPane();
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(420, 380);

        list = new ListView<Pane>();
        panes = FXCollections.observableArrayList();

        // anchor_pane.getStylesheets().add("../CSS/Conv.css");
        for (int i = 0; i < allPosts.size(); i++) {
            FlowPane p1 = new FlowPane();
            p1.setId("Peer_contentPane");
            p1.setVgap(6);
            p1.setHgap(100);
            p1.setPrefWrapLength(400);

            //show the image of the post creator
            ByteArrayInputStream in = new ByteArrayInputStream(db.getPeer(allPosts.get(i).getUsername()).getProf_pic());
            BufferedImage read = null;
            try {
                read = ImageIO.read(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profPic.setImage(SwingFXUtils.toFXImage(read, null));
            //Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
            ImageView img = new ImageView();
            img.setFitHeight(30);
            img.setFitWidth(30);
            img.setPreserveRatio(true);
            img.setImage(SwingFXUtils.toFXImage(read, null));

            Label label3 = new Label(allPosts.get(i).getUsername(), img);
            label3.setTextFill(Color.web("#483D8B"));
            label3.setFont(Font.font("Cambria", 14));
            p1.getChildren().addAll(label3);
            //Button b1 = new Button("Select");
            //b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            //b1.setId(String.valueOf(allPeerUsernames.get(i)));//set the user name as the id of the button

            FlowPane p2 = new FlowPane();
            p2.setId("Post_contentPane");
            p2.setVgap(6);
            p2.setHgap(60);

            p2.setPrefWrapLength(400);
            Label label4 = new Label(allPosts.get(i).getContent());
            label4.setId("label_post_content");
            label4.setPadding(new Insets(10.0, 5.0, 10.0, 5.0));
            label4.setTextFill(Color.web("#191970"));
            label4.setFont(Font.font("Cambria", 14));
            p2.getChildren().addAll(label4);

            Reply lst_reply=allPosts.get(i).getLastReply();

            FlowPane p3=new FlowPane();
            p3.setId("ReplyPane");
            p3.setVgap(6);
            p3.setHgap(80);
            p3.setPrefWrapLength(370);
            p3.setBorder(new Border(new BorderStroke(Color.DARKGREY,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            if(lst_reply!=null) {
                //p3 = new FlowPane();
                //p3.setId("ReplyPane");
                ByteArrayInputStream in2 = new ByteArrayInputStream(db.getPeer(lst_reply.getUsername()).getProf_pic());
                BufferedImage read2 = null;
                try {
                    read2 = ImageIO.read(in2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Image image1 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                ImageView img2 = new ImageView();
                img2.setFitHeight(25);
                img2.setFitWidth(25);
                img2.setPreserveRatio(true);
                img2.setImage(SwingFXUtils.toFXImage(read2, null));


                Label label5 = new Label(lst_reply.getUsername(), img2);
                //Label label5 = new Label(allPosts.get(i).getLastReply().getUsername(), img2);
                label5.setTextFill(Color.web("#0076a3"));
                label5.setFont(Font.font("Cambria", 12));
                Label label6 = new Label(lst_reply.getContent());
                //Label label6 = new Label(allPosts.get(i).getLastReply().getContent());
                Button b1 = new Button("View All");
                b1.setAlignment(Pos.BASELINE_RIGHT);
                b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");


                b1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {

                            Parent root = FXMLLoader.load(getClass().getResource("../View/ReplyView.fxml"));
                            Stage stage = new Stage();
                            stage.setTitle("All Replies for this post");
                            stage.setScene(new Scene(root, 369.0, 465.0));
                            stage.show();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                //b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
                p3.getChildren().addAll(label5, label6, b1);

            }else{
                Label label5 = new Label("No Reply for the Post Yet");
                //Label label5 = new Label(allPosts.get(i).getLastReply().getUsername(), img2);
                label5.setTextFill(Color.web("#0076a3"));
                label5.setFont(Font.font("Cambria", 12));
                p3.getChildren().addAll(label5);
            }

            FlowPane p4 = new FlowPane();
            p4.setVgap(6);
            p4.setHgap(37);
            p4.setPrefWrapLength(400);

            TextField textField2 = new TextField ();
            textField2.setId("text"+String.valueOf(i));
            textField2.setPromptText("Your Reply");

            textField2.setPrefWidth(250);
            Button b2=new Button();
            b2.setId("btn"+String.valueOf(i));
            b2.setText("Share");
            b2.setAlignment(Pos.BOTTOM_RIGHT);
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {

                    int j =Integer.parseInt(b2.getId().substring(b2.getId().length()-1));
                    System.out.println(j);
                    Post p=allPosts.get(j);
                    Reply reply=new Reply(Owner.myUsername,textField2.getText(),p.getPostID(),Reply.getNextReplyId(p.getPostID()));
                    System.out.println("got the reply "+textField2.getText());
                    p.addReply(reply);
                    p.sendReply(reply);//This is to sent the reply to required peers
                    //This reply is not yet sent to any one
                }
            });

            p4.getChildren().addAll(textField2,b2);
            p4.setAlignment(Pos.CENTER);

            Separator separator1 = new Separator();
            separator1.setId("sep1");
            separator1.setMaxWidth(400);
            separator1.setHalignment(HPos.LEFT);



            FlowPane flow = new FlowPane(Orientation.VERTICAL);
            flow.setColumnHalignment(HPos.LEFT); // align labels on left
            flow.setVgap(1);
            flow.setPrefWrapLength(140);
            flow .getChildren().add(p1);
            flow .getChildren().add(p2);
            flow .getChildren().add(p3);
            flow .getChildren().add(p4);
            flow .getChildren().add(separator1);

            panes.add(flow);
        }
        list.setItems(panes);
        s1.setContent(list);
        post_pane.getChildren().add(s1);

    }

    private void showDefaultProfPic(){
        Image image = new Image(getClass().getResourceAsStream("default.png"));
        //File file = new File("src/Box13.jpg");
        //Image image = new Image(file.toURI().toString());
        profPic.setImage(image);
    }
    private void showProfPic( byte[] byteArray){
        //byte[] byteArray = p.getImage();
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage read = null;
        try {
            read = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        profPic.setImage(SwingFXUtils.toFXImage(read, null));

    }


}

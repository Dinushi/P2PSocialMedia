package sample.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import sample.DBHandler.DbHandler;
import sample.EventHandler.NewPeerListner;
import sample.EventHandler.PostHandler;
import sample.Model.Conversation;
import sample.Model.Owner;
import sample.Model.Post;
import sample.Model.Reply;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;
import java.util.Optional;

public class HomeController{
    public static HomeController homeController;
    public static boolean notALoginOut;


    @FXML
    private TextArea txt_post;
    @FXML
    private Button btn_post_share;
    @FXML
    private Button btn_post_cancel;
    @FXML
    private Pane profPic_pane;
    @FXML
    private Pane wall;
    @FXML
    private ImageView img;
    @FXML
    private Label lbl_username;

    ArrayList<Post> allPosts;

    ListView<Pane> list ;
    ObservableList<Pane> panes;
    ScrollPane s1;
    private Post currnt_post;

    public HomeController () {
        homeController=this;
        notALoginOut=true;
    }

    public void initialize() {

        if(Validator.thisPeer.getProf_pic()!=null){
            ByteArrayInputStream in2 = new ByteArrayInputStream(Validator.thisPeer.getProf_pic());
            BufferedImage read = null;
            try {
                read = ImageIO.read(in2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Image image1 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database

            img.setFitHeight(115);
            img.setFitWidth(115);
            img.setPreserveRatio(true);
            img.setImage(SwingFXUtils.toFXImage(read, null));

        }else{
            Image image = new Image(getClass().getResourceAsStream("default.png"));//modify code to get the image from database
            img.setImage(image);
            img.setFitHeight(115);
            img.setFitWidth(115);
            img.setPreserveRatio(true);
            Glow glow =new Glow();
            glow.setLevel(0.9);
            img.setEffect(glow);
        }
        lbl_username.setText(Validator.thisPeer.getUsername());


        //displayPosts(Post.selectAllPosts());
        allPosts= Post.selectAllPosts();
        System.out.println("At postsrC:"+allPosts);
        //s1 = new ScrollPane();
        displayPosts(allPosts);


    }
    private void displayPosts(ArrayList<Post> allPosts){

        System.out.println("At postsrC:"+allPosts);

        s1 = new ScrollPane();
        s1.setId("scroll-pane ");
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(475, 433);


        list = new ListView<Pane>();
        panes = FXCollections.observableArrayList();

        // anchor_pane.getStylesheets().add("../CSS/Conv.css");
        for (int i = 0; i < allPosts.size(); i++) {
            currnt_post=allPosts.get(i);

            FlowPane p1 = new FlowPane();
            p1.setId("Peer_contentPane");
            p1.setVgap(6);
            p1.setHgap(100);
            p1.setPrefWrapLength(400);

            DbHandler db=new DbHandler();
            ImageView img;
            byte[] prof_pic=db.getPeer(allPosts.get(i).getUsername()).getProf_pic();
            db.closeConnection();
            if(prof_pic!=null){
                ByteArrayInputStream in = new ByteArrayInputStream(prof_pic);
                BufferedImage read = null;
                try {
                    read = ImageIO.read(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                img = new ImageView();
                img.setFitHeight(30);
                img.setFitWidth(30);
                img.setPreserveRatio(true);
                img.setImage(SwingFXUtils.toFXImage(read, null));
            }else{
                Image image2 = new Image(getClass().getResourceAsStream("default.png"));//modify code to get the image from database
                img = new ImageView(image2);
                img.setFitHeight(30);
                img.setFitWidth(30);
                img.setPreserveRatio(true);
            }


            Label label3 = new Label(allPosts.get(i).getUsername(), img);
            label3.setTextFill(Color.web("#000000"));
            label3.setFont(Font.font("Cambria", 14));
            p1.getChildren().addAll(label3);
            p1.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
            //Button b1 = new Button("Select");
            //b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            //b1.setId(String.valueOf(allPeerUsernames.get(i)));//set the user name as the id of the button

            FlowPane p2 = new FlowPane();
            p2.setId("Post_contentPane");
            p2.setVgap(6);
            p2.setHgap(60);

            //p2.setPrefWrapLength(400);
            Label label4 = new Label(allPosts.get(i).getContent());
            label4.setId("label_post_content");
            label4.setPadding(new Insets(10.0, 5.0, 10.0, 5.0));
            label4.setTextFill(Color.web("#191970"));
            label4.setFont(Font.font("Cambria", 14));
            label4.setWrapText(true);
            p2.getChildren().addAll(label4);
            p2.setStyle("-fx-background-color: rgba(47,100,95,0.5); -fx-background-radius: 10;");

            Reply lst_reply=allPosts.get(i).getLastReply();

            FlowPane p3=new FlowPane();
            p3.setId("ReplyPane");
            p3.setVgap(6);
            p3.setHgap(30);
            p3.setPrefWrapLength(300);
            p3.setPadding(new Insets(0, 0, 0, 15));
            p3.setBorder(new Border(new BorderStroke(Color.DARKGREY,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            if(lst_reply!=null) {
                //p3 = new FlowPane();
                //p3.setId("ReplyPane");
                DbHandler db2=new DbHandler();
                ImageView imgg;
                byte[] prof_pic_reply=db2.getPeer(lst_reply.getUsername()).getProf_pic();
                db.closeConnection();
                if(prof_pic_reply!=null){
                    ByteArrayInputStream in = new ByteArrayInputStream(prof_pic_reply);
                    BufferedImage read = null;
                    try {
                        read = ImageIO.read(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                    imgg = new ImageView();
                    imgg.setFitHeight(25);
                    imgg.setFitWidth(25);
                    imgg.setPreserveRatio(true);
                    imgg.setImage(SwingFXUtils.toFXImage(read, null));
                }else{
                    Image image3 = new Image(getClass().getResourceAsStream("default.png"));//modify code to get the image from database
                    imgg = new ImageView(image3);
                    imgg.setFitHeight(30);
                    imgg.setFitWidth(30);
                    imgg.setPreserveRatio(true);
                }



                //p3.setVgap(6);
                //p3.setHgap(80);
                //p3.setPrefWrapLength(370);
                //p3.setBorder(new Border(new BorderStroke(Color.DARKGREY,
                //BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

                Label label5 = new Label(lst_reply.getUsername(), imgg);
                //Label label5 = new Label(allPosts.get(i).getLastReply().getUsername(), img2);
                label5.setTextFill(Color.web("#F0FFFF"));
                label5.setFont(Font.font("Cambria", 12));
                Label label6 = new Label(lst_reply.getContent());
                //Label label6 = new Label(allPosts.get(i).getLastReply().getContent());
                Button b1 = new Button("View All");
                b1.setAlignment(Pos.BASELINE_RIGHT);
                b1.setId(String.valueOf(i));
                b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");


                b1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        try {
                            int j =Integer.parseInt(b1.getId());
                            System.out.println("selected Peer id"+j);
                            Post post=allPosts.get(j);

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/ReplyView.fxml"));

                            // Create a controller instance
                            ReplyController replyController=new ReplyController(post);

                            // Set it in the FXMLLoader
                            loader.setController(replyController);
                            Parent root= loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("All Replies");
                            Scene scene=new Scene(root, 327.0, 425.0);
                            stage.setScene(scene);
                            scene.getStylesheets().add(getClass().getResource("../CSS/Reply.css").toString());
                            stage.show();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                //b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
                p3.getChildren().addAll(label5, label6, b1);
                p3.setStyle("-fx-background-color: rgba(66,98,100,0.5); -fx-background-radius: 10;");

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
            textField2.setOpacity(0.6);

            textField2.setPrefWidth(250);
            Button b2=new Button();
            b2.setId("btn"+String.valueOf(i));
            b2.setText("Share");
            b2.setOpacity(0.6);
            b2.setAlignment(Pos.BOTTOM_RIGHT);
            b2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {

                    int j =Integer.parseInt(b2.getId().substring(b2.getId().length()-1));
                    System.out.println(j);
                    Post p=allPosts.get(j);
                    Reply reply=new Reply(Owner.myUsername,textField2.getText(),p.getPostID(),Reply.getNextReplyId(p.getPostID()));
                    reply.setReply_creator(Validator.username);
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
            flow.setAlignment(Pos.BOTTOM_CENTER);
            flow.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5); -fx-background-radius: 10;");
            panes.add(flow);
        }
        list.setItems(panes);
        s1.setContent(list);
        s1.setOpacity(0.7);
        s1.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        if(!wall.getChildren().isEmpty()){
            wall.getChildren().remove(0);
        }
        wall.getChildren().add(s1);
        wall.setStyle("-fx-background-color: transparent; -fx-padding: 10;");

    }


    public void showNewPost(Post post){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    allPosts.add(0,post);
                    displayPosts(allPosts);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void sharePost(ActionEvent event){
        DbHandler db=new DbHandler();
        int post_id=db.getMyMaxPostID()+1;
        db.closeConnection();
        btn_post_share.setText("");
        Post p=new Post(Owner.myUsername,txt_post.getText(),post_id);
        //load the post view again
        allPosts.add(0,p);
        displayPosts(allPosts);
        Runnable run = new Runnable() {
            public void run() {
                System.out.println("Try to sent post using another thread");
                PostHandler.sentThePostToPeers(p);
            }
        };
        new Thread(run).start();
    }
    public void pressPostCancel(ActionEvent event){
        txt_post.setText("");
    }

    public void pressPeerRequests(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/PeerRequest.fxml"));
            Stage stage = new Stage();
            stage.setTitle("New Peer Requests");
            stage.setScene(new Scene(root, 335.0, 470.0));
            stage.show();
        }catch(IOException e) {
            e.printStackTrace();
        }

    }
    public void pressMyProfile(ActionEvent event2){
        System.out.println("MyProfile is opening");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        if(MyProfileController.myProfileController==null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/MyProfile.fxml"));
                Stage stage = new Stage();
                stage.setTitle("My Profile");
                stage.setScene(new Scene(root, 650.0, 574.0));

                stage.setOnHiding(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("closing the edit profile");
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText("Closing edit Profile");
                                alert.setContentText("Do you want to save all changes?");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK && MyProfileController.hasEdited) {

                                    Validator.thisPeer = MyProfileController.myProfileController.edited_peer;

                                   //A seperate thread is assigned to save these data to the database
                                    Runnable run = new Runnable() {
                                        public void run() {
                                                System.out.println("Try to work using another thread");
                                                Validator.thisPeer.updateprofiledata();
                                                NewPeerListner.sendPeerProfileChanges();
                                        }
                                    };
                                    new Thread(run).start();
                                    notALoginOut=false;
                                    HomeController.homeController=null;
                                    try {
                                        Parent root = FXMLLoader.load(getClass().getResource("../View/AppHome.fxml"));
                                        Stage stage = new Stage();
                                        stage.setTitle("PeerNet");
                                        Scene scene=new Scene(root, 747, 601);
                                        stage.setScene(scene);
                                        scene.getStylesheets().add(getClass().getResource("../CSS/Home.css").toString());
                                        stage.show();
                                        ((Node) (event2.getSource())).getScene().getWindow().hide();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    //also send these changes to a all connected Peers
                                //} else {
                                    // ... user chose CANCEL or closed the dialog
                                }
                                MyProfileController.myProfileController=null;


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
    public void pressConversation(ActionEvent event){
        //PostHandler.gotAPost(new Post("Chin","Hii all come here plz"));
       // System.out.println("Coversations are opening");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        if(ChatController.chatController==null){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/Chats.fxml"));
                Stage stage = new Stage();
                stage.setTitle("My Conversations");
                stage.setScene(new Scene(root, 369.0, 465.0));

                // Hide this current window (if this is what you want)
                //((Node) (event.getSource())).getScene().getWindow().hide();

                stage.setOnHiding(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("Conversation application is closed by closed button");
                                ChatController.chatController=null;

                            }
                        });
                    }
                });
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
    public void pressViewDiscoverdPeers(ActionEvent event) {
        System.out.println("Discoverd Peers are opening");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        if (DiscoveredPeerViewController.discoveredPeerViewController== null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/DiscoveredPeerView.fxml"));
                Stage stage = new Stage();
                stage.setTitle("All Discoverd Peers");
                stage.setScene(new Scene(root, 343.0, 515.0));

                stage.setOnHiding(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("Closing the edit peerList");
                                DiscoveredPeerViewController.discoveredPeerViewController=null;

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
    public void pressViewPeerProfile(ActionEvent event) {
        System.out.println("peer Profiles is opening");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        if (ConnectedPeerListController.connectedPeerListController == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/ViewPeerProfileList.fxml"));
                Stage stage = new Stage();
                stage.setTitle("All Connected Peers");
                stage.setScene(new Scene(root, 313.0, 544.0));

                stage.setOnHiding(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("Closing the edit peerList");
                                ConnectedPeerListController.connectedPeerListController=null;

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

    public void ViewOnlinePeers(ActionEvent event) {
        System.out.println("Opening online peer list");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        if (OnlinePeerController.onlinePeerController == null) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/OnlinePeers.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Online Peers");
                stage.setScene(new Scene(root, 313.0, 544.0));

                stage.setOnHiding(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("Closing the Online peer List");
                                OnlinePeerController.onlinePeerController=null;
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

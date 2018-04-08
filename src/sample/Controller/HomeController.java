package sample.Controller;

import javafx.application.Platform;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Model.Conversation;
import sample.Model.Owner;
import sample.Model.Post;

import java.io.IOException;
import java.util.ArrayList;

public class HomeController{
    @FXML private AnchorPane anchor_pane;


    @FXML
    private TextArea txtpeerPost1;
    @FXML
    private TextArea txtpeerPost2;
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

    ListView<Pane> list ;
    ObservableList<Pane> panes;
    ScrollPane s1;

    public void initialize() {
        //displayPosts(Post.selectAllPosts());

        ArrayList<Post> allPosts= Post.selectAllPosts();
        System.out.println("At postsrC:"+allPosts);

        s1 = new ScrollPane();
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(400, 422);

        list = new ListView<Pane>();
        panes = FXCollections.observableArrayList();

       // anchor_pane.getStylesheets().add("../CSS/Conv.css");
        for (int i = 0; i < allPosts.size(); i++) {

            FlowPane p1 = new FlowPane();
            p1.setVgap(6);
            p1.setHgap(60);
            p1.setPrefWrapLength(400);
            Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
            ImageView img = new ImageView(image);
            img.setFitHeight(25);
            img.setFitWidth(25);
            img.setPreserveRatio(true);

            Label label3 = new Label(allPosts.get(i).getUsername(), img);
            label3.setTextFill(Color.web("#483D8B"));
            label3.setFont(Font.font("Cambria", 14));
            p1.getChildren().addAll(label3);
            //Button b1 = new Button("Select");
            //b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            //b1.setId(String.valueOf(allPeerUsernames.get(i)));//set the user name as the id of the button

            FlowPane p2 = new FlowPane();
            p2.setVgap(6);
            p2.setHgap(60);
            //p2.setBorder(new Border(new BorderStroke(Color.grayRgb(4),
                    //BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            p2.setPrefWrapLength(200);
            Label label4 = new Label(allPosts.get(i).getContent());
            label4.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
            label4.setTextFill(Color.web("#0076a3"));
            label4.setFont(Font.font("Cambria", 12));
            p2.getChildren().addAll(label4);

            Separator separator2 = new Separator();

            FlowPane p3 = new FlowPane();
            p3.setVgap(6);
            p3.setHgap(60);
            p3.setPrefWrapLength(200);
            Label label5 = new Label(allPosts.get(i).getLastReply().getUsername(), img);
            label5.setTextFill(Color.web("#0076a3"));
            label5.setFont(Font.font("Cambria", 12));
            Label label6 = new Label(allPosts.get(i).getLastReply().getContent());
            Button b1 = new Button("View All");
            b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");


            /*
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
            */
            //b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p3.getChildren().addAll(label5,label6,b1);
            Separator separator1 = new Separator();





            FlowPane flow = new FlowPane(Orientation.VERTICAL);
            flow.setColumnHalignment(HPos.LEFT); // align labels on left
            flow.setPrefWrapLength(110);
            flow .getChildren().add(p1);
            flow .getChildren().add(p2);
            flow .getChildren().add(p3);
            flow .getChildren().add(separator1);
            //VBox vbox = new VBox();
            //vbox.setPadding(new Insets(10));
            //vbox.setSpacing(8);
            //vbox.getChildren().add(p1);
           // vbox.getChildren().add(p2);
           // vbox.getChildren().add(p3);
            //panes.add(vbox);
            panes.add(flow);
        }
        list.setItems(panes);
        s1.setContent(list);
        wall.getChildren().add(s1);
        Circle circle = new Circle(14);

        final ImageView imageView = new ImageView("http://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Sunset_2007-1.jpg/640px-Sunset_2007-1.jpg");
        final Circle clip = new Circle(300, 200, 200);
        imageView.setClip(clip);
        profPic_pane.getChildren().add(imageView);
    }
    private void displayPosts(ArrayList<Post> allPosts){

        System.out.println("At postsrC:"+allPosts);

        s1 = new ScrollPane();
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(400, 422);

        list = new ListView<Pane>();

        // anchor_pane.getStylesheets().add("../CSS/Conv.css");
        for (int i = 0; i < allPosts.size(); i++) {

            FlowPane p1 = new FlowPane();
            p1.setVgap(6);
            p1.setHgap(60);
            p1.setPrefWrapLength(400);
            Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
            ImageView img = new ImageView(image);
            img.setFitHeight(25);
            img.setFitWidth(25);
            img.setPreserveRatio(true);

            Label label3 = new Label(allPosts.get(i).getUsername(), img);
            label3.setTextFill(Color.web("#483D8B"));
            label3.setFont(Font.font("Cambria", 14));
            p1.getChildren().addAll(label3);
            //Button b1 = new Button("Select");
            //b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");
            //b1.setId(String.valueOf(allPeerUsernames.get(i)));//set the user name as the id of the button

            FlowPane p2 = new FlowPane();
            p2.setVgap(6);
            p2.setHgap(60);
            //p2.setBorder(new Border(new BorderStroke(Color.grayRgb(4),
            //BorderStrokeStyle.DASHED, CornerRadii.EMP
            // TY, BorderWidths.DEFAULT)));
            p2.setPrefWrapLength(200);
            Label label4 = new Label(allPosts.get(i).getContent());
            label4.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
            label4.setTextFill(Color.web("#0076a3"));
            label4.setFont(Font.font("Cambria", 12));
            p2.getChildren().addAll(label4);

            Separator separator2 = new Separator();

            FlowPane p3 = new FlowPane();
            p3.setVgap(6);
            p3.setHgap(60);
            p3.setPrefWrapLength(200);
            Label label5 = new Label(allPosts.get(i).getLastReply().getUsername(), img);
            label5.setTextFill(Color.web("#0076a3"));
            label5.setFont(Font.font("Cambria", 12));
            Label label6 = new Label(allPosts.get(i).getLastReply().getContent());
            Button b1 = new Button("View All");
            b1.setStyle("-fx-font: 09 arial; -fx-base: #a9a9a9;");


            /*
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
            */
            //b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p3.getChildren().addAll(label5,label6,b1);
            Separator separator1 = new Separator();





            FlowPane flow = new FlowPane(Orientation.VERTICAL);
            flow.setColumnHalignment(HPos.LEFT); // align labels on left
            flow.setPrefWrapLength(110);
            flow .getChildren().add(p1);
            flow .getChildren().add(p2);
            flow .getChildren().add(p3);
            flow .getChildren().add(separator1);
            //VBox vbox = new VBox();
            //vbox.setPadding(new Insets(10));
            //vbox.setSpacing(8);
            //vbox.getChildren().add(p1);
            // vbox.getChildren().add(p2);
            // vbox.getChildren().add(p3);
            //panes.add(vbox);
            panes.add(flow);
        }
        list.setItems(panes);
        s1.setContent(list);
        wall.getChildren().add(s1);
    }


    public void showNewPost(Post post){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));
        //this method is called by a normal thread.But we can handle UI compoents only using Application thread.
        //so this tunlater allows the normal thread to run on application thraead for a while and do the task
        /*
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String content = post.getContent();
                    String date = post.getDate_created().toString();
                    Parent root = FXMLLoader.load(getClass().getResource("../View/AppHome.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Home Form");
                    stage.setScene(new Scene(root, 400, 600));
                    stage.show();
                    txtpeerPost1.setText(content);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        */
    }
    public void sharePost(ActionEvent event){
        Post p=new Post(Owner.myUsername,txt_post.getText());
        p.addToDb();
        p.sendPost();
    }
    public void pressPostCancel(ActionEvent event){
        txt_post.setText("");
    }

    public void pressPeerRequests(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/PeerRequest.fxml"));
            Stage stage = new Stage();
            stage.setTitle("New Peer Requests");
            stage.setScene(new Scene(root, 369.0, 465.0));
            stage.show();
        }catch(IOException e) {
            e.printStackTrace();
        }

    }
    public void pressMyProfile(ActionEvent event){
        System.out.println("MyProfile is opening");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/MyProfile.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My Profile");
            stage.setScene(new Scene(root, 369.0, 465.0));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void pressConversation(ActionEvent event){
        System.out.println("Coversations are opening");
        //Parent root = FXMLLoader.load(getClass().getResource("../View/Register.fxml"));

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/Chats.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My Conversations");
            stage.setScene(new Scene(root, 369.0, 465.0));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

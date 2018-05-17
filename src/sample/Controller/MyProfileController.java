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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.DBHandler.DbHandler;
import sample.Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class MyProfileController{

    @FXML
    private Button btn_changeProfPic;
    @FXML
    private Pane post_pane;
    @FXML
    private ImageView profPic;
    @FXML
    private Label lbl_myUername;
    @FXML
    private Label  lbl_status;
    @FXML
    private TextField txt_status;
    @FXML
    private Pane editPane;
    @FXML
    private Label lbl_fullName;
    @FXML
    private Label lbl_birthday;
    @FXML
    private Label lbl_hometown;


    public static MyProfileController myProfileController;

    ArrayList<Post> allPosts;
    ListView<Pane> list ;
    ObservableList<Pane> panes;
    ScrollPane s1;
    Peer edited_peer;//editions are stored here.
    Peer thisPeer;
    DbHandler db;
    public static boolean hasEdited;

    public MyProfileController () {
        myProfileController=this;
    }

    public void initialize() {
        thisPeer=Validator.thisPeer;
        edited_peer=new Peer(thisPeer.getUsername(),thisPeer.getIp(),thisPeer.getPort(),thisPeer.isJoined());
        edited_peer.setHometown(thisPeer.getHometown());
        edited_peer.setFullname(thisPeer.getFullname());
        edited_peer.setProf_pic(thisPeer.getProf_pic());
        edited_peer.setStatus(thisPeer.getStatus());
        edited_peer.setBday(thisPeer.getBday());
        edited_peer.setGender(thisPeer.getGender());
        this.hasEdited=false;


        if(thisPeer.getProf_pic()==null){
            showDefaultProfPic();

        }else{
            showProfPic(thisPeer.getProf_pic());
        }
        this.lbl_status.setText(thisPeer.getStatus());
        this.lbl_myUername.setText(thisPeer.getUsername());
        this.lbl_fullName.setText(thisPeer.getFullname());
        this.lbl_birthday.setText(thisPeer.getBday().toString());
        this.lbl_hometown.setText(thisPeer.getHometown());

        this.db=new DbHandler();
        allPosts=db.getPosts(true,thisPeer.getUsername());

        displayPosts(allPosts);
        db.closeConnection();

    }
    private void displayPosts(ArrayList<Post> allPosts){

        System.out.println("At postsrC:"+allPosts);

        s1 = new ScrollPane();
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(433, 401);

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
            ImageView img;
            byte[] prof_pic=db.getPeer(allPosts.get(i).getUsername()).getProf_pic();
            if(prof_pic!=null){
                ByteArrayInputStream in = new ByteArrayInputStream(prof_pic);
                BufferedImage read = null;
                try {
                    read = ImageIO.read(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profPic.setImage(SwingFXUtils.toFXImage(read, null));
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

                ImageView img2;
                byte[] replier_prof_pic=db.getPeer(lst_reply.getUsername()).getProf_pic();
                if(prof_pic!=null){
                    ByteArrayInputStream in = new ByteArrayInputStream(replier_prof_pic);
                    BufferedImage read = null;
                    try {
                        read = ImageIO.read(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                    img2 = new ImageView();
                    img2.setFitHeight(25);
                    img2.setFitWidth(25);
                    img2.setPreserveRatio(true);
                    img2.setImage(SwingFXUtils.toFXImage(read, null));
                }else{
                    Image image23= new Image(getClass().getResourceAsStream("default.png"));//modify code to get the image from database
                    img2 = new ImageView(image23);
                    img2.setFitHeight(25);
                    img2.setFitWidth(25);
                    img2.setPreserveRatio(true);
                }

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

                            Parent root = FXMLLoader.load(getClass().getResource("/sample/View/ReplyView.fxml"));
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


    //This will store the edited data of the project
    public void   pressProfDataEdit(ActionEvent e) {
        TextField t1=new TextField();
        t1.setPromptText("Your New Name");

        TextField t2=new TextField();
        t2.setPromptText("Your Hometown");

        PasswordField t3=new PasswordField();
        t3.setPromptText("New Password");

        PasswordField t4=new PasswordField();
        t4.setPromptText("Re-type new Password");

        // Create the DatePicker.
        DatePicker datePicker=new DatePicker();
        datePicker.setPromptText("Your Birthday");

        // Add some action (in Java 8 lambda syntax style).
        /*
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            System.out.println("Selected date: " + date);
        });
        */

        Button b1=new Button();
        b1.setText("Update");
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(t1.getText()!=null){
                    edited_peer.setFullname(t1.getText());
                    lbl_fullName.setText(edited_peer.getFullname());
                    hasEdited=true;
                }
                if(t2.getText()!=null){
                    edited_peer.setHometown(t2.getText());
                    lbl_hometown.setText(edited_peer.getHometown());
                    hasEdited=true;
                }
                if(datePicker.getValue()!=null){
                    edited_peer.setBday(Date.valueOf(datePicker.getValue()));
                    lbl_birthday.setText(edited_peer.getBday().toString());
                    hasEdited=true;
                }
                if(t3.getText()!= null && t3.getText()==t4.getText()){
                    String newPassword=t3.getText();
                    //need the whole code to update the password in the TheisUserTxt file
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Password Mismatch. Password is not updated");
                    /*alert.setContentText("You didn't select a file!");*/
                    alert.show();
                }


            }
        });
        Button b2=new Button();
        b2.setText("Cancel");
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");
            }
        });

        FlowPane flow = new FlowPane(Orientation.VERTICAL);
        flow.setColumnHalignment(HPos.LEFT); // align labels on left
        flow.setVgap(1);
        flow.setPrefWrapLength(170);
        flow .getChildren().add(t1);
        flow .getChildren().add(t2);
        flow .getChildren().add(datePicker);
        flow .getChildren().add(t3);
        flow .getChildren().add(t4);
        flow .getChildren().add(b1);
        flow .getChildren().add(b2);
        editPane.getChildren().add(flow);

    }
    public void  pressStatusUpdate(ActionEvent event) {
        String new_status=this.txt_status.getText();
        this.lbl_status.setText(new_status);
        edited_peer.setStatus(new_status);
        hasEdited=true;
        txt_status.setText("");

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

    public void pressChangeProfPic(ActionEvent event) {
        FileChooser choose = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extFilterJFIF= new FileChooser.ExtensionFilter("JFIF files (*.jfif)", "*.jfif");

        //f.length() < 3 * (1024 * 1024));
        choose.getExtensionFilters().addAll(extFilterJPG, extFilterPNG,extFilterJFIF);
        File file = choose.showOpenDialog(null);

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            byte[] b;
            try (ByteArrayOutputStream out = new ByteArrayOutputStream(262144)) {
                ImageIO.write(bufferedImage, "jpg", out);
                out.flush();
                b = out.toByteArray();
            }
            if(file.length()<20*1024){
                edited_peer.setProf_pic(b);
                hasEdited=true;
                showProfPic(b);
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("The selected file should be less than 16KB");
                /*alert.setContentText("You didn't select a file!");*/
                alert.show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        //FileChooserSample f_chooser=new FileChooserSample();
        //f_chooser.showFileChooser();
/*
        System.out.println("file has choosed");
        File file = f_chooser.getFile();
        if(file!=null) {

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                byte[] b;
                try (ByteArrayOutputStream out = new ByteArrayOutputStream(262144)) {
                    ImageIO.write(bufferedImage, "jpg", out);
                    out.flush();
                    b = out.toByteArray();
                }
                edited_peer.setProf_pic(b);
                showProfPic(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/
    }


}
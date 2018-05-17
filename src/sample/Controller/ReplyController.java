package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sample.DBHandler.DbHandler;
import sample.Model.Post;
import sample.Model.Reply;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReplyController implements Initializable {
    @FXML
    private Pane ReplyPane;

    ArrayList<Reply> all_replies;

    ListView<Pane> list ;
    ObservableList<Pane> panes;
    ScrollPane s1;
    private Post post;

    public ReplyController(Post post) {
        this.post=post;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        all_replies= post.getReplies();

        System.out.println("At postsrC:"+all_replies);

        s1 = new ScrollPane();
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(301, 359);

        list = new ListView<Pane>();
        panes = FXCollections.observableArrayList();

        for (int i = 0; i < all_replies.size(); i++) {

            FlowPane p3 = new FlowPane();
            p3.setVgap(6);
            p3.setHgap(60);
            p3.setPrefWrapLength(100);

            DbHandler db=new DbHandler();
            ImageView img;
            byte[] prof_pic=db.getPeer(all_replies.get(i).getUsername()).getProf_pic();
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
                img.setFitHeight(25);
                img.setFitWidth(25);
                img.setPreserveRatio(true);
                img.setImage(SwingFXUtils.toFXImage(read, null));
            }else{
                Image image2 = new Image(getClass().getResourceAsStream("default.png"));//modify code to get the image from database
                img = new ImageView(image2);
                img.setFitHeight(25);
                img.setFitWidth(25);
                img.setPreserveRatio(true);
            }

            Label label5 = new Label(all_replies.get(i).getUsername(), img);
            label5.setTextFill(Color.web("#0076a3"));
            label5.setFont(Font.font("Cambria", 12));

            Label label6 = new Label(all_replies.get(i).getContent());

            p3.getChildren().addAll(label5,label6);
            Separator separator1 = new Separator();

            FlowPane flow = new FlowPane(Orientation.VERTICAL);
            flow.setColumnHalignment(HPos.LEFT); // align labels on left
            flow.setPrefWrapLength(110);
            flow .getChildren().add(p3);
            flow .getChildren().add(separator1);

            panes.add(flow);
        }
        list.setItems(panes);
        s1.setContent(list);
        s1.setOpacity(0.7);
        ReplyPane.getChildren().add(s1);
    }
}

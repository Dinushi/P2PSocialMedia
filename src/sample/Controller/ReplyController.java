package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import sample.Model.Post;
import sample.Model.Reply;

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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        all_replies= post.getReplies();

        System.out.println("At postsrC:"+all_replies);

        s1 = new ScrollPane();
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(400, 422);

        list = new ListView<Pane>();
        panes = FXCollections.observableArrayList();

        for (int i = 0; i < all_replies.size(); i++) {

            FlowPane p3 = new FlowPane();
            p3.setVgap(6);
            p3.setHgap(60);
            p3.setPrefWrapLength(200);

            Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
            ImageView img = new ImageView(image);
            img.setFitHeight(25);
            img.setFitWidth(25);
            img.setPreserveRatio(true);

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
        ReplyPane.getChildren().add(s1);

    }

    void initData(Post post) {
        this.post=post;
    }



}

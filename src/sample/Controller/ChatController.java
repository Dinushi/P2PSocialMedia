package sample.Controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
import sample.DBHandler.DbHandler;
import sample.EventHandler.ConversationHandler;
import sample.Model.Conversation;
import sample.Model.Message;
import sample.Model.Owner;
import sample.Model.Peer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatController {
    public static ChatController chatController;

    @FXML
    public TabPane tabPane;//this is the pane which hold all tabs
    private ArrayList<Tab> allAddedTabs=new ArrayList<>();
    //ListView<Pane> list;
    //ObservableList<Pane> panes ;

    private ArrayList<Conversation> allConversations;


    public ChatController(){
        chatController=this;
    }

    public void initialize() {
        allConversations=ConversationHandler.getAllConversations();
        this.showConversations(allConversations);
/*
        Tab tab = new Tab("All Conversations");
        // Rectangle rect = new Rectangle(200, 200, Color.RED);
        //VBox v=new VBox();
        ///ListView<Pane> list = new ListView<Pane>();
        list = new ListView<Pane>();
        ///ObservableList<Pane> panes = FXCollections.observableArrayList();
        panes = FXCollections.observableArrayList();

        for (int i = 0; i < 5; i++) {
            // StackPane p1= new StackPane();
            //p1.setAlignment(Pos.CENTER);

            FlowPane p1 = new FlowPane();
            p1.setVgap(8);
            p1.setHgap(4);
            p1.setPrefWrapLength(300);
            Image image = new Image(getClass().getResourceAsStream("dinu.jpg"));
            ImageView img = new ImageView(image);
            img.setFitHeight(50);
            img.setFitWidth(50);
            img.setPreserveRatio(true);

            Label label3 = new Label("Dinushi1234", img);
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 15));


            Label label4 = new Label("Please come to the University tomorrow.I want to talk to to you");
            label4.setFont(Font.font("Arial", 12));
            label4.setWrapText(true);
            Button b1 = new Button("View");

            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    int numTabs = tabPane.getTabs().size();
                    Tab tab = new Tab("Tab " + (numTabs + 1));
                    tabPane.getTabs().add(tab);
                }
            });
            b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p1.getChildren().addAll(label3, label4, b1);

            panes.add(p1);
            //v.getChildren().setAll(p1);
            //tab.setContent(v);
        }
        list.setItems(panes);
        tab.setContent(list);
        tabPane.getTabs().add(tab);

        //A label with the text element
        //Label label2 = new Label("Dinushi1234");
        //A label with the text element and graphical icon

        //Label label1 = new Label("Dinushi1234");
        //Label label3 = new Label("Dinushi1234", img);
        //label3.setMinWidth(50);
        //label3.setMinHeight(50);
        //p1.getChildren().addAll(label1,label3 );
        //v.getChildren().setAll(p1);
        //tab.setContent(v);

        //ListView<Pane> list = new ListView<Pane>();
        //ObservableList<Pane> panes = FXCollections.observableArrayList (
        // p1,new Pane());

        ObservableList<String> items = FXCollections.observableArrayList(
                "Single", "Double", "Suite", "Family App");
        //list.setItems(panes);

        //ScrollPane s1 = new ScrollPane();
        //s1.setPrefSize(120, 120);
        //s1.setContent(rect);
        //tab.setContent(list);
        // tabPane.getTabs().add(tab);
*/
    }

    public void showConversations(ArrayList <Conversation> all_conversations){
        System.out.println("Number of tabs "+tabPane.getTabs());
        if(tabPane.getTabs().size()>0){
            tabPane.getTabs().remove(0);
            System.out.println("Number of tabs after removal"+tabPane.getTabs());
        }

        Tab tab = new Tab("All Conversations");
        // Rectangle rect = new Rectangle(200, 200, Color.RED);
        //VBox v=new VBox();
        ///ListView<Pane> list = new ListView<Pane>();
        ListView<Pane> list = new ListView<Pane>();
        ///ObservableList<Pane> panes = FXCollections.observableArrayList();
        ObservableList<Pane> panes = FXCollections.observableArrayList();

        for (int i = all_conversations.size()-1; i>=0 ; i--) {
        //for (int i = 0; i < all_conversations.size(); i++) {
            // StackPane p1= new StackPane();
            //p1.setAlignment(Pos.CENTER);

            FlowPane p1 = new FlowPane();
            p1.setVgap(8);
            p1.setHgap(4);
            p1.setPrefWrapLength(300);

            Label label3;
            if(all_conversations.get(i).getChatPartner().size()==1){
                Image image_user = new Image(getClass().getResourceAsStream("dinu.jpg"));
                ImageView img = new ImageView(image_user);
                img.setFitHeight(50);
                img.setFitWidth(50);
                img.setPreserveRatio(true);

                label3 = new Label(all_conversations.get(i).getTitle(), img);
            }else{
                Image image_group = new Image(getClass().getResourceAsStream("../Images/chat.jpg"));
                ImageView img2 = new ImageView(image_group);
                img2.setFitHeight(50);
                img2.setFitWidth(50);
                img2.setPreserveRatio(true);

                label3 = new Label(all_conversations.get(i).getTitle(), img2);
            }
            //Label label3 = new Label(allConversations.get(i).getTitle(), img);
            label3.setTextFill(Color.web("#0076a3"));
            label3.setFont(Font.font("Cambria", 15));


            Label label4 = new Label(all_conversations.get(i).getMessages().get(0).getContent());
            label4.setFont(Font.font("Arial", 12));
            label4.setWrapText(true);
            Button b1 = new Button("View");
            b1.setId("btn"+String.valueOf(i));


            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    int j =Integer.parseInt(b1.getId().substring(b1.getId().length()-1));
                    System.out.println(j);
                    Conversation conv=all_conversations.get(j);
                    ChatController.chatController.createNewConversationTab(conv,false);
                    //int numTabs = tabPane.getTabs().size();
                    //Tab tab = new Tab("Tab " + (numTabs + 1));
                    //tabPane.getTabs().add(tab);
                }
            });
            b1.setStyle("-fx-font: 10 arial; -fx-base: #b6e7c9;");
            p1.getChildren().addAll(label3, label4, b1);

            panes.add(p1);
            //v.getChildren().setAll(p1);
            //tab.setContent(v);
        }
        list.setItems(panes);
        tab.setContent(list);
        list.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Pane>() {
                    @Override
                    public void changed(ObservableValue<? extends Pane> observable, Pane oldValue, Pane newValue) {
                        System.out.println("selection changed");
                    }
                });
        tabPane.getTabs().add(tab);

        //A label with the text element
        //Label label2 = new Label("Dinushi1234");
        //A label with the text element and graphical icon

        //Label label1 = new Label("Dinushi1234");
        //Label label3 = new Label("Dinushi1234", img);
        //label3.setMinWidth(50);
        //label3.setMinHeight(50);
        //p1.getChildren().addAll(label1,label3 );
        //v.getChildren().setAll(p1);
        //tab.setContent(v);

        //ListView<Pane> list = new ListView<Pane>();
        //ObservableList<Pane> panes = FXCollections.observableArrayList (
        // p1,new Pane());

        ObservableList<String> items = FXCollections.observableArrayList(
                "Single", "Double", "Suite", "Family App");
        //list.setItems(panes);

        //ScrollPane s1 = new ScrollPane();
        //s1.setPrefSize(120, 120);
        //s1.setContent(rect);
        //tab.setContent(list);
        // tabPane.getTabs().add(tab);


    }
    public  void createNewConversationTab(Conversation conv,boolean isNewConversation ){
        boolean go=true;
        Tab simillar_tab_t=null;
        for(Tab t: allAddedTabs){
            if(t.getId()!=conv.getTitle()){
                go=true;
            }else{
                go=false;
                simillar_tab_t=t;
                return;
            }
        }
        if(!go){
           tabPane.getTabs().remove(simillar_tab_t);
        }

        Tab tab1 = new Tab(conv.getTitle());
        tab1.setId(conv.getTitle());
        tab1.setId(conv.getTitle());


        FlowPane flow = new FlowPane(Orientation.VERTICAL);
        flow.setColumnHalignment(HPos.LEFT); // align labels on left
        flow.setVgap(5);
        flow.setPrefWrapLength(360);

        //contain the names of the chat partners
        ArrayList<Peer> chatPartners=conv.getChatPartner();
        FlowPane partnerView=new FlowPane();
        partnerView.setHgap(5);

        for(Peer peer:chatPartners){
            Label lbl=new Label(peer.getUsername()+" ");
            lbl.setTextFill(Color.web("#0076a3"));
            lbl.setFont(Font.font("Cambria", 15));
            partnerView.getChildren().add(lbl);
           // Label label3 = new Label(, img2);if possible add a image of the user
        }
        flow .getChildren().add(partnerView);

        //scroll pane will contain all meaasges of the chat

        ArrayList<Message> chatMessages=conv.getMessages();
        ScrollPane s1 = new ScrollPane();
        s1.setFitToHeight(true);
        s1.setFitToWidth(true);
        s1.setPrefSize(455, 275);

        ListView<Pane> list_inner = new ListView<Pane>();
        ObservableList<Pane> panes_inner  = FXCollections.observableArrayList();

        for(Message msg:chatMessages){
            FlowPane p3=new FlowPane();

            Label label5;
            if(msg.getMsg_creator()!=Owner.myUsername){
                Image image1 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                ImageView img1 = new ImageView(image1);
                img1.setFitHeight(25);
                img1.setFitWidth(25);
                img1.setPreserveRatio(true);

                label5 = new Label(msg.getContent(), img1);
                label5.setTextFill(Color.web("#87CEFA"));
                label5.setFont(Font.font("Cambria", 12));
            }else{
                Image image2 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                ImageView img2 = new ImageView(image2);
                img2.setFitHeight(25);
                img2.setFitWidth(25);
                img2.setPreserveRatio(true);

                label5 = new Label(msg.getContent(), img2);

                label5.setTextFill(Color.web("#20B2AA"));
                label5.setFont(Font.font("Cambria", 12));

            }

            p3.getChildren().add(label5);
            panes_inner.add(p3);
        }
        list_inner.setItems(panes_inner);
        s1.setContent(list_inner);
        flow .getChildren().add(s1);

        //A seperate pane to write  new message
        FlowPane createMessage=new FlowPane();
        createMessage.setHgap(5);

        TextField textField2 = new TextField ();
        textField2.setPromptText("Your Message");

        textField2.setPrefWidth(250);
        Button b2=new Button();
        b2.setText("Send");
        b2.setAlignment(Pos.BOTTOM_RIGHT);



        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {


                String msg_content=textField2.getText();
                Message msg=new Message(Owner.myUsername,msg_content);
                msg.setConversation_id(conv.getConversation_id());
                msg.setConversation_initiator(conv.getConversation_initiator());

                DbHandler db=new DbHandler();
                if(isNewConversation){
                    Conversation conv2=db.getAConversation(conv.getConversation_id(),conv.getConversation_initiator().getUsername());
                    if(conv2!=null){
                        msg.setMessage_id(db.getMaxMsgId(conv)+1);
                    }else{
                        msg.setMessage_id(1);
                    }
                }else{
                    msg.setMessage_id(db.getMaxMsgId(conv)+1);
                }
                db.closeConnection();
                msg.setSent_received("Sent");
                msg.setStatus("NotDelivered");
                System.out.println("New message is created");
                conv.addMessage(msg);
                if(isNewConversation){
                    ConversationHandler.sendTheInitialConversation(conv);//at the conv creation conv is send together with 1st message
                    textField2.setText("");
                }else{
                    ConversationHandler.sendMessageToPartners(conv,msg);
                    textField2.setText("");
                }
                ListView<Pane> list_inner1 = new ListView<Pane>();
                ObservableList<Pane> panes_inner1  = FXCollections.observableArrayList();
                ArrayList<Message> chatMessages1=conv.getMessages();
                for(Message msg2:chatMessages1){
                    FlowPane p4=new FlowPane();

                    Label label6;
                    if(msg2.getMsg_creator()!=Owner.myUsername){
                        System.out.println("A message from a partner");
                        Image image2 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                        ImageView img2 = new ImageView(image2);
                        img2.setFitHeight(25);
                        img2.setFitWidth(25);
                        img2.setPreserveRatio(true);

                        label6 = new Label(msg2.getContent(), img2);
                        label6.setTextFill(Color.web("#87CEFA"));
                        label6.setFont(Font.font("Cambria", 12));
                    }else{
                        System.out.println("This is a my message");
                        Image image2 = new Image(getClass().getResourceAsStream("dinu.jpg"));//modify code to get the image from database
                        ImageView img2 = new ImageView(image2);
                        img2.setFitHeight(25);
                        img2.setFitWidth(25);
                        img2.setPreserveRatio(true);

                        label6 = new Label(msg2.getContent(), img2);
                        label6.setTextFill(Color.web("#20B2AA"));
                        label6.setFont(Font.font("Cambria", 12));

                    }

                    p4.getChildren().add(label6);
                    panes_inner1.add(p4);
                }
                list_inner1.setItems(panes_inner1);
                s1.setContent(list_inner1);

            }
        });

        createMessage.getChildren().addAll(textField2,b2);
        createMessage.setAlignment(Pos.CENTER);
        flow.getChildren().add(createMessage);

        tab1.setContent(flow);
        tabPane.getTabs().add(tab1);
        allAddedTabs.add(tab1);

    }

    @FXML
    public void addTab() {
        int numTabs = tabPane.getTabs().size();
        Tab tab = new Tab("Tab " + (numTabs + 1));
        tabPane.getTabs().add(tab);

    }

    @FXML
    private void listTabs() {
        tabPane.getTabs().forEach(tab -> System.out.println(tab.getText()));
        System.out.println();
    }
    public void createNewConversation(ActionEvent event){
        //peersla prompt karaan page ekak pennana
        //methana mulu page ekama generate karanan
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/addPartner.fxml"));
            Stage stage2 = new Stage();
            stage2.setTitle("New Conversation");
            stage2.setScene(new Scene(root, 315, 566));
            stage2.show();
            Conversation conv=new Conversation();


            for (String partner : AddPartnerController.selectedPartners) {
                conv.selectPeer(partner);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void gotANewConversation(Conversation conv){
        allConversations.add(conv);
        this.showConversations(allConversations);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                allConversations.add(conv);
                ChatController.chatController.showConversations(allConversations);

            }
        });
        /*
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("../View/addPartner.fxml"));
                    Stage stage2 = new Stage();
                    stage2.setTitle("New Conversation");
                    stage2.setScene(new Scene(root, 282, 466));
                    stage2.show();
                    Conversation conv=new Conversation();
                    allConversations.add(conv);
                    this.showConversations(allConversations);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        */

    }
}


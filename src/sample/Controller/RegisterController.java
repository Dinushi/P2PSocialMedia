package sample.Controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.CommunicationHandler.ClientConnection;
import sample.CommunicationHandler.PeerConnection;
import sample.DBHandler.CreateDB;
import sample.EventHandler.NewPeerListner;
import sample.Model.Owner;
import sample.Model.Peer;
import sample.Model.ThisPeer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sample.Controller.Validator.thisPeer;

public class RegisterController {
    public static RegisterController registerController;


    @FXML
    private TextField name;

    @FXML
    private TextField userName;

    @FXML
    private TextField hometown;

    @FXML
    private RadioButton check_male;

    @FXML
    private RadioButton check_female;

    @FXML
    private DatePicker birthday;

    @FXML
    private TextField password;

    @FXML
    private Button btn_submit;

    @FXML
    private TextField re_password;

    public RegisterController(){
        registerController=this;
    }

    //private Client client;######################
    private ClientConnection client;//??????????????
    Window owner;

    public void pressSubmit(ActionEvent event) {
        System.out.println("Submit Button Pressed");

        this.owner = btn_submit.getScene().getWindow();

        String fullname=null;
        String username=null;
        String homeTown=null;
        String pWord=null;
        String re_pWord=null;

        System.out.println("Submit Button Pressed next");
        boolean NoError=true;

            fullname=this.name.getText();

            username=this.userName.getText();
            System.out.println("Username"+username);
            System.out.println("Username"+username.length());

            System.out.println("fullname"+fullname.isEmpty());

            homeTown=this.hometown.getText();
            pWord=this.password.getText();
            re_pWord=this.re_password.getText();
            System.out.println("hiame"+homeTown.isEmpty());
            System.out.println("pwame"+pWord.isEmpty());
            System.out.println("rephiame"+re_pWord.isEmpty());
            /*

            if(fullname.length()==0 || username.length()==0  || homeTown.length()==0  || pWord.length()==0  || re_pWord.length()==0  ){
                error=true;
                System.out.println("Submit Button Pressed jjjjjjjjjjj");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Form Error");
                alert.setContentText("All required fields are not filled");
                alert.showAndWait();
            }
            */



        //if only all raws are filled rest of the code is continued
        if(NoError){
            System.out.println("came in");
            //boolean isCorrectAll =validateTheUserData(fullname,username,homeTown,pWord,re_pWord);
            boolean isCorrectAll=true;
            if(isCorrectAll){
                //sent username,ip,port to bootstrap server
                boolean reg_success=this.connectToServer();

                if(reg_success){
                    //create the database
                    CreateDB dbCreator=new CreateDB();
                    //create the thisUser
                    this.createThisUser();

                    PeerConnection peerCon=PeerConnection.getPeerConnection();
                    peerCon.createTheSocketListner();//a listen socket will be created to listen on requests.
                    PeerConnection.initialLogin=true;
                    //assign this work to a thrad to allow re transmissions and continue login page dispaly quickly
                    NewPeerListner.sendJoinRequestToDiscoverdPeersFromBs();//peer connections are set with discoverd peers
                    //this.sendConfirmation();
                    this.showRegisterSuccess(event);
                }

                try {
                    createTheUserCredentials();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }


    }
    public void pressCancel(ActionEvent event){
        this.name.setText("");
        this.userName.setText("");
        this.hometown.setText("");
        this.password.setText("");
        this.re_password.setText("");
    }

    public boolean validateTheUserData(String fullname, String username, String homeTown, String pWord, String re_pWord){
        boolean re1=this.validateUsername(username);
        boolean re2=this.validatePasswordEntered(pWord,re_pWord);
        boolean re3=this.validateFullName_HomeTown(fullname,true);
        boolean re4=this.validateFullName_HomeTown(homeTown,false);
        if(re1 && re2 && re3 && re4){
            return true;
        }
        return false;
    }

    public boolean validatePasswordEntered(String pWord,String re_pWord) {
        if (pWord != re_pWord) {
            /*
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Password Mismatch. Re-type your password");
            password.setText("");
            re_password.setText("");
            alert.showAndWait();
            */
            return false;
        } else {
            return validatePassword(pWord);
            //check whether password contain maximum 8 characters with letters,symbols and numbers
        }
    }

    /*
     must contain one digit, one lower case char, one upper case char, a symbol from _@#$%,
      length should be within 6 to 15 chars.
     */
    public boolean validatePassword(String password_entered) {
        Pattern pswNamePtrn = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#$%]).{6,15})");
        Matcher mtch = pswNamePtrn.matcher(password_entered);
        if(mtch.matches()){
            return true;
        }
        /*
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Password doesn't match to correct format");
        alert.setContentText("It must contain one digit, one lower case char, one upper case char,a symbol from _@#$%,\n" +
                        " length should be within 6 to 15 chars ");
        alert.showAndWait();
        password.setText("");
        re_password.setText("");
        */
        return false;
    }
    //allows lower and Upper case alphanumeric characters, allows '-', '_'
    public boolean validateUsername(String username) {
        Pattern usrNamePtrn = Pattern.compile("^[a-zA-Z0-9_-]{6,14}$");
        Matcher mtch = usrNamePtrn.matcher(username);
        if(mtch.matches()){
            return true;
        }
        /*
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Username doesn't match to correct format");
        alert.setContentText("Username can contain only lower case, upper case letters and '-' , '_' symbols ");
        alert.showAndWait();
        userName.setText("");
        */
        return false;
    }

    public boolean validateFullName_HomeTown(String str,boolean isFullName) {
        Pattern usrNamePtrn = Pattern.compile("^[a-zA-Z ]*");
        Matcher mtch = usrNamePtrn.matcher(str);
        if(mtch.matches()){
            return true;
        }
        /*
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        if(isFullName){
            alert.setHeaderText("Your Full Name seems Incorrect");
        }else{
            alert.setHeaderText("Your Hometown seems Incorrect");
        }
        alert.setContentText("It can contain only upper case and lower case letters ");
        alert.showAndWait();
        if(isFullName){
            name.setText("");
        }else{
            hometown.setText("");
        }
        */
        return false;
    }


    private boolean connectToServer() {
        this.client=new ClientConnection(userName.getText());
        int result=client.getResultFromBs();
        if(result==0){
            RegisterController.registerController.usernameAlreadyExist();
            return false;
        }else if(result==0){
            RegisterController.registerController.IpPortAlreadyExist();
            return  false;
        }
        return true;


    }

    private void createThisUser() {
        Owner.myIP =client.getLocal_address();
        Owner.myUsername=userName.getText();
        Owner.myPort=client.getLocal_port();
        //validator has this variable
        thisPeer=new Peer(Owner.myUsername,Owner.myIP,Owner.myPort,false);

        thisPeer.setFullname(name.getText());//but you have to validate before using
        thisPeer.setHometown(hometown.getText());
        thisPeer.setBday(Date.valueOf(birthday.getValue()));
        if(check_male.isSelected())
            thisPeer.setGender("M");
        else
            thisPeer.setGender("F");
        boolean result=thisPeer.insertToDb();
        if(result){
            System.out.println("This user has been successfully stored.");
        }else{
            System.out.println("This user failed to store.");
        }

    }
    private void createTheUserCredentials() throws IOException{

        BufferedWriter writer = new BufferedWriter(new FileWriter("ThisUser.txt"));
        writer.write(userName.getText());
        writer.newLine();
        writer.write(String.valueOf(client.getLocal_address().toString().split("/")[1]));
        writer.newLine();
        writer.write(String.valueOf(client.getLocal_port()));
        writer.newLine();
        writer.write(String.valueOf(password.getText()));
        writer.close();
        System.out.print("User Credentials has added in the file");

    }
    private void sendConfirmation(){

        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, this.owner, "Successfully registered in the Bootstrap Server!",
                "Now you can Login");

    }
    private void showRegisterSuccess(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/RegConfirmation.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Register Confirmation");
            Scene scene=new Scene(root, 351, 318);
            stage.setScene(scene);
            stage.show();
            // Hide this current window
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //private void createClientConnection() {
      //  ClientConnection connection = new ClientConnection("127.0.0.1", 55555, (Serializable data) -> {
        //    Platform.runLater(() -> {
          //      userName.appendText(data.toString());
         //   });
        //});

   // }
    public void pressBackToLogin(ActionEvent event){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login P2P social media");
            Scene scene=new Scene(root, 400, 300);
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("../CSS/Login.css").toString());
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void usernameAlreadyExist(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("The username you selected is already taken");
                alert.setContentText("Please give another username");
                alert.showAndWait();
                userName.setText("");

            }
        });
    }
    //check whether does it need platform.run later
    public void IpPortAlreadyExist(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("You are already Registered with this IP and Port");
                alert.setContentText("Please register Once Again");
                alert.showAndWait();
            }
        });
    }
}


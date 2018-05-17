package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationRule;
import org.testfx.framework.junit.ApplicationTest;
import sample.Controller.LoginController;
import sample.Controller.PeerProfileController;
import sample.Model.Peer;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextFlowMatchers.hasText;

public class MainTest extends ApplicationTest {

    LoginController loCont;
    @Override
    public void start (Stage stage) throws Exception {

        Parent mainNode = FXMLLoader.load(Main.class.getResource("View/Login.fxml"));

        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();




        /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/PeerProfile.fxml"));
            loCont=loader.getController();
            Parent root= loader.load();
            stage.setTitle("Peer Profile");
            stage.setScene(new Scene(root, 650.0, 574.0));
            stage.show();
            stage.toFront();

*/

    }
    @Test
    public void registorPress () {
        //Label label = (Label) GuiTest.find("#label");
        clickOn("#btn_register");

        // assertThat(label.getText(), is("This is a test!"));
    }



    @Before
    public void setUp () throws Exception {
    }


    @Test
    public  void testCancelButtion(){
        clickOn("#userName");
        write("Boo");
        clickOn("#password");
        write("12345");
        clickOn("#btn_cancel");
        TextField txt_username = (TextField) GuiTest.find("#userName");
        TextField txt_password = (TextField) GuiTest.find("#password");
        assertThat(txt_username.getText(), is(""));
        assertThat(txt_password.getText(), is(""));
    }

    @Test
    public void testCorrectInput () {
        //Label label = (Label) GuiTest.find("#label");

        clickOn("#userName");
        write("Boo");
        clickOn("#password");
        write("123");

        clickOn("#btn_login");

       // assertThat(label.getText(), is("This is a test!"));
    }





    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }


}
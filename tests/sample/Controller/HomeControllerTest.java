package sample.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import sample.Main;

import static org.junit.Assert.*;

public class HomeControllerTest extends ApplicationTest {

    @Override
    public void start (Stage stage) throws Exception {

        Parent mainNode = FXMLLoader.load(Main.class.getResource("/sample/View/AppHome.fxml"));

        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }


    @Test
    public void showNewPost() {
    }

    @Test
    public void sharePost() {
    }

    @Test
    public void pressPostCancel() {
    }

    @Test
    public void pressPeerRequests() {
    }

    @Test
    public void pressMyProfile() {
    }

    @Test
    public void pressConversation() {
    }

    @Test
    public void pressViewDiscoverdPeers() {
    }

    @Test
    public void pressViewPeerProfile() {
    }

    @Test
    public void viewOnlinePeers() {
    }
}
package sample.Controller;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class FileChooserSample{
    private byte[] imageInByte;//this contain image converted to bytes
    private File file;
    private String imageFilePath;

    private Desktop desktop = Desktop.getDesktop();


    public void showFileChooser() {
        final Stage stage = new Stage();
        stage.setTitle("File Chooser Sample");

        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Open a Picture...");
        final Button openMultipleButton = new Button("Open Pictures...");

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        configureFileChooser(fileChooser);
                        file = fileChooser.showOpenDialog(stage);
                        //try {
                            //imageFilePath = getFile().toURI().toURL().toString();
                        //} catch (MalformedURLException e1) {
                            //e1.printStackTrace();
                        //}
                        if (getFile() != null) {
                            openFile(getFile());
                        }else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText("Please Select a File");
                            /*alert.setContentText("You didn't select a file!");*/
                            alert.showAndWait();
                        }
                    }
                });

        openMultipleButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        configureFileChooser(fileChooser);
                        List<File> list =
                                fileChooser.showOpenMultipleDialog(stage);
                        if (list != null) {
                            for (File file : list) {
                                openFile(file);
                            }
                        }
                    }
                });


        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 1);
        GridPane.setConstraints(openMultipleButton, 1, 1);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, openMultipleButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }


    private static void configureFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private void openFile(File file) {
        ByteArrayOutputStream baos=null;
        try {

                BufferedImage originalImage =
                        ImageIO.read(file);

                baos = new ByteArrayOutputStream();
                ImageIO.write( originalImage, "jpg", baos );
                baos.flush();
                this.imageInByte = baos.toByteArray();
                System.out.println("write this image in DB");

                //save imageInByte as blob in database


            desktop.open(file);
        }catch(IOException e){
        System.out.println(e.getMessage());
        }finally{
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //close database connection
        }
    }

    public byte[] getImageInByte() {
        return imageInByte;
    }

    public File getFile() {
        return file;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }
}
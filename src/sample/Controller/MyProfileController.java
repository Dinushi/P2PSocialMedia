package sample.Controller;


import javafx.event.ActionEvent;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;
import java.io.File;

public class MyProfileController {


    public void pressChangeProfPic(ActionEvent event) {
        FileChooserUI fChooser=new FileChooserUI() {
            @Override
            public FileFilter getAcceptAllFileFilter(JFileChooser fc) {
                return null;
            }

            @Override
            public FileView getFileView(JFileChooser fc) {
                return null;
            }

            @Override
            public String getApproveButtonText(JFileChooser fc) {
                return null;
            }

            @Override
            public String getDialogTitle(JFileChooser fc) {
                return null;
            }

            @Override
            public void rescanCurrentDirectory(JFileChooser fc) {

            }

            @Override
            public void ensureFileIsVisible(JFileChooser fc, File f) {

            }
        };

    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface.Controllers;

import Interface.SceneHolder;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Liviu
 */
public class SharePageController implements Initializable {
    
    @FXML private Button addFilesButton, removeButton, backButton;
    @FXML private ListView sharedFilesList;
    FileChooser fileChooser;
    
    @FXML
    private void buttonAction(ActionEvent e){
        Button b = (Button)e.getSource();
        
        if(b == addFilesButton){
            List<File> selected;
            selected = fileChooser.showOpenMultipleDialog(b.getScene().getWindow());
            
            if (selected != null) {
                FileSharing.FileSharingManager.getInstance().addFiles(new ArrayList<File>(selected));
                for (int i = 0; i < selected.size(); i++) {
                    sharedFilesList.getItems().add(selected.get(i));
                }
            }
        }
        
        if(b == removeButton){
            if(sharedFilesList.getSelectionModel().getSelectedItem() != null){
                ArrayList<File> al = new ArrayList<>();
                al.add(((File)sharedFilesList.getSelectionModel().getSelectedItem()));
                sharedFilesList.getItems().remove(sharedFilesList.getSelectionModel().getSelectedItem());
                FileSharing.FileSharingManager.getInstance().removeFiles(al);
            }
        }
        
        if(b == backButton){
            ((Stage)b.getScene().getWindow()).setScene(SceneHolder.getInstance().getMainPage());
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                ArrayList<File> sharedFiles = FileSharing.FileSharingManager.getInstance().getSharedFiles();
                
                for(int i = 0; i<sharedFiles.size(); i++)
                    synchronized(sharedFilesList.getItems()){
                        sharedFilesList.getItems().add(sharedFiles.get(i));
                    }
            }
        });
        
        t.start();
        
        fileChooser = new FileChooser();
    }    
    
}

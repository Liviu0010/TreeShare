/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface.Controllers;

import Interface.SceneHolder;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Liviu
 */
public class MainPageController implements Initializable {

    @FXML private ListView resultList;
    @FXML private Button searchButton, downloadingButton, downloadButton, sharedFilesButton;
    @FXML private TextField searchTextField;
    
    
    @FXML
    private void buttonAction(ActionEvent e){
        Button b = (Button)e.getSource();
        
        if(b == searchButton){
            System.out.println("Searching for: "+searchTextField.getText());
        }
        
        if(b == downloadingButton){
            System.out.println("Switching to downloading page");
            ((Stage)b.getScene().getWindow()).setScene(SceneHolder.getInstance().getDownloadingPage());
        }
        
        if(b == downloadButton){
            System.out.println("Downloading: "+resultList.getSelectionModel().getSelectedItem());
        }
        
        if(b == sharedFilesButton){
            System.out.println("Switching to Shared Files page");
            ((Stage)b.getScene().getWindow()).setScene(SceneHolder.getInstance().getSharePage());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resultList.getItems().addAll("one", "two", "three", "four");
    }    
    
}

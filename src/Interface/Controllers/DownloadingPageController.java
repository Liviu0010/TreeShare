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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Liviu
 */
public class DownloadingPageController implements Initializable {

    @FXML private Button stopButton, backButton;
    @FXML private ListView downloadingList;
    
    @FXML
    private void buttonAction(ActionEvent e){
        Button b = (Button)e.getSource();
        
        if(b == stopButton){
            System.out.println("Stopping download: "+downloadingList.getSelectionModel().getSelectedItem());
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
        downloadingList.getItems().addAll("I like", "turtles", "and", "also", "I like trains");
    }    
    
}

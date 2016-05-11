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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Liviu
 */
public class ConnectPageController implements Initializable {
    
    @FXML
    private Button connectButton;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button b = (Button)event.getSource();
        
        if(b == connectButton){
            ((Stage)connectButton.getScene().getWindow()).setScene(SceneHolder.getInstance().getMainPage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

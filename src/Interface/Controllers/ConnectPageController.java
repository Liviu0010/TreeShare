/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface.Controllers;

import Interface.SceneHolder;
import Networking.Async.ParentConnection;
import Networking.NetworkManager;
import Networking.NetworkNode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Liviu
 */
public class ConnectPageController implements Initializable {
    
    @FXML
    private Button connectButton, rootButton;
    
    @FXML private TextField portTextField, addressTextField;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button b = (Button)event.getSource();
        
        if(b == connectButton || b == rootButton){
            if(b == connectButton){
                ParentConnection.getInstance().setParent(new NetworkNode(addressTextField.getText(), Integer.parseInt(portTextField.getText())));
                ParentConnection.getInstance().start();
            }
            
            NetworkManager.getInstance().startListening();
            ((Stage)connectButton.getScene().getWindow()).setScene(SceneHolder.getInstance().getMainPage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

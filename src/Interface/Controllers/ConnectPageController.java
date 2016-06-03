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
    
    @FXML private TextField portTextField, addressTextField, netConListenerTextField, 
            netMsgTextField, downloadReqTextField, fileUploadTextField;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button b = (Button)event.getSource();
        int netConListenerPort, netMsgPort, downloadReqPort, fileTransferPort, connectToPort;
        
        if(portTextField.getText().equals(""))
            connectToPort = Integer.parseInt(portTextField.getPromptText());
        else
            connectToPort = Integer.parseInt(portTextField.getText());
        
        if(netConListenerTextField.getText().equals(""))
            netConListenerPort = Integer.parseInt(netConListenerTextField.getPromptText());
        else
            netConListenerPort = Integer.parseInt(netConListenerTextField.getText());
        
        if(netMsgTextField.getText().equals(""))
            netMsgPort = Integer.parseInt(netMsgTextField.getPromptText());
        else
            netMsgPort = Integer.parseInt(netMsgTextField.getText());
        
        if(downloadReqTextField.getText().equals(""))
            downloadReqPort = Integer.parseInt(downloadReqTextField.getPromptText());
        else
            downloadReqPort = Integer.parseInt(downloadReqTextField.getText());
        
        if(fileUploadTextField.getText().equals(""))
            fileTransferPort = Integer.parseInt(fileUploadTextField.getPromptText());
        else
            fileTransferPort = Integer.parseInt(fileUploadTextField.getText());
        
        NetworkManager.getInstance().setNetworkConnectionListenerPort(netConListenerPort);
        NetworkManager.getInstance().setNetworkMessagesPort(netMsgPort);
        NetworkManager.getInstance().setDownloadRequestListenerPort(downloadReqPort);
        NetworkManager.getInstance().setFileTransferPort(fileTransferPort);
        
        System.out.println(NetworkManager.getInstance().getNetworkConnectionListenerPort());
        System.out.println(NetworkManager.getInstance().getNetworkMessagesPort());
        System.out.println(NetworkManager.getInstance().getDownloadRequestListenerPort());
        System.out.println(NetworkManager.getInstance().getFileTransferPort());
        System.out.println(connectToPort);
        
        if(b == connectButton || b == rootButton){
            if(b == connectButton){
                NetworkManager.getInstance().connectTo(new NetworkNode(addressTextField.getText(), connectToPort));
            }
            
            NetworkManager.getInstance().startListening();
            ((Stage)connectButton.getScene().getWindow()).setScene(SceneHolder.getInstance().getMainPage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}

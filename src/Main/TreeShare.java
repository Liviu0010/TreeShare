/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Interface.SceneHolder;
import Networking.Async.DownloadConnection;
import Networking.Async.ParentConnection;
import Networking.Async.UploadListener;
import Networking.NetworkManager;
import Networking.NetworkNode;
import Networking.OwnedFile;
import java.io.File;
import java.net.InetAddress;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Liviu
 */
public class TreeShare extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Interface/Pages/ConnectPage.fxml"));
        
        Scene scene = new Scene(root);
        
        SceneHolder.getInstance().setConnectPage(scene);
        SceneHolder.getInstance().setMainPage(new Scene(FXMLLoader.load(getClass().getResource("/Interface/Pages/MainPage.fxml"))));
        SceneHolder.getInstance().setDownloadingPage(new Scene(FXMLLoader.load(getClass().getResource("/Interface/Pages/DownloadingPage.fxml"))));
        SceneHolder.getInstance().setSharePage(new Scene(FXMLLoader.load(getClass().getResource("/Interface/Pages/SharePage.fxml"))));
        
//TESTING GROUNDS
        
        //System.out.println(InetAddress.getLocalHost().getHostAddress());
        
        //ParentConnection.getInstance().setParent(new NetworkNode("localhost"));
        //ParentConnection.getInstance().start();
        
        //NetworkManager.getInstance().startListening();
        
        File file = new File("C:/Users/Liviu/Pictures/Screenshots/Screenshot (1).png");
        NetworkNode owner = new NetworkNode("192.168.0.132");
        //DownloadConnection downloadConnection = new DownloadConnection(new OwnedFile(file, owner));
        //downloadConnection.start();
        UploadListener.getInstance().start();
//END        
        stage.setWidth(900);
        stage.setHeight(700);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop(){
        NetworkManager.getInstance().stopAll();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

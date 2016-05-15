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
import Networking.Messages.SearchResult;
import Networking.NetworkManager;
import Networking.NetworkNode;
import Networking.OwnedFile;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
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
               
        stage.setWidth(900);
        stage.setHeight(700);
        stage.setScene(scene);
        stage.show();
        //NetworkManager.getInstance().updateSearchList(new SearchResult(new ArrayList<OwnedFile>()));
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

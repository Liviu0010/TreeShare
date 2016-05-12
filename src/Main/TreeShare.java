/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Interface.SceneHolder;
import java.net.InetAddress;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        
        //System.out.println(InetAddress.getLocalHost().getHostAddress());
        
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

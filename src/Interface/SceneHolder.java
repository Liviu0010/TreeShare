/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import javafx.scene.Scene;

/**
 *
 * @author Liviu
 */
public class SceneHolder{
    private Scene connectPage, mainPage, sharePage, downloadingPage;
    private static SceneHolder instance;
    
    private SceneHolder(){
        
    }
    
    static public SceneHolder getInstance(){
        if(instance == null)
            instance = new SceneHolder();
        return instance;
    }
    
    public void setConnectPage(Scene connectPage){
        this.connectPage = connectPage;
    }
    
    public Scene getConnectPage(){
        return connectPage;
    }
    
    public void setMainPage(Scene mainPage){
        this.mainPage = mainPage;
    }
    
    public Scene getMainPage(){
        return mainPage;
    }
    
    public void setSharePage(Scene sharePage){
        this.sharePage = sharePage;
    }
    
    public Scene getSharePage(){
        return sharePage;
    }
    
    public void setDownloadingPage(Scene downloadingPage){
        this.downloadingPage = downloadingPage;
    }
    
    public Scene getDownloadingPage(){
        return downloadingPage;
    }
}

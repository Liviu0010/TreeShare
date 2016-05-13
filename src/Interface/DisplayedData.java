/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import java.io.File;
import java.util.List;
import javafx.scene.control.ListView;

/**
 *
 * @author Liviu
 */
public class DisplayedData {
    private static DisplayedData instance;
    ListView searchResults, sharedFiles, downloadingFiles;
    
    private DisplayedData(){
        
    }
    
    public static DisplayedData getInstance(){
        if(instance == null){
            instance = new DisplayedData();
        }
        
        return instance;
    }
    
    public void setSearchResults(ListView toSet){
        searchResults = toSet;
    }
    
    public void setSharedFiles(ListView toSet){
        sharedFiles = toSet;
    }
    
    public void setDownloadingFiles(ListView toSet){
        downloadingFiles = toSet;
    }
    
    public ListView getSearchResults(){
        return searchResults;
    }
    
    public ListView getsharedFiles(){
        return sharedFiles;
    }
    
    public ListView getDownloadingFiles(){
        return downloadingFiles;
    }
}

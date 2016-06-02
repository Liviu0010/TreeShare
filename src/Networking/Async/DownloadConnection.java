/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import Interface.DisplayedData;
import Networking.Messages.ConnectionResponse;
import Networking.NetworkManager;
import Networking.OwnedFile;
import Networking.PrivateRequests.DownloadRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.ListView;

/**
 *
 * @author Liviu
 */
public class DownloadConnection extends NetworkThread {
    Socket connection;
    OwnedFile downloading;
    FileOutputStream fileOutputStream;
    InputStream inputStream;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    boolean fileVisible;
    int index;
    
    
    public DownloadConnection(OwnedFile downloading){
        this.downloading = downloading;
    }
    
    @Override
    public void run(){
        ConnectionResponse response;
        DownloadRequest request;
        byte[] buffer = new byte[2048];
        int count, last = 0;
        long total = 0;
        
        
        try {
            if(connection == null){
                return;
            }
            connection = new Socket(downloading.getOwner().getAddress(), NetworkManager.getInstance().getDownloadRequestListenerPort());
            
            objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
            objectInputStream = new ObjectInputStream(connection.getInputStream());
            
            request = new DownloadRequest(downloading);
            objectOutputStream.writeObject(request);
            
            response = (ConnectionResponse) objectInputStream.readObject();
            
            objectOutputStream.close();
            objectInputStream.close();
            connection.close();
            
            if(response.getAssignedPort() == -1){
                return;
            }
            
            connection = new Socket(downloading.getOwner().getAddress(), response.getAssignedPort());
            
            inputStream = connection.getInputStream();
            fileOutputStream = new FileOutputStream(downloading.getName());
            
            while(running && (count = inputStream.read(buffer)) > 0 ){
                if((int) (((double)total)/downloading.size() * 100) - last > 0){
                    updateDownloadStatus((int) (((double)total)/downloading.size() * 100));
                    last = (int) (((double)total)/downloading.size() * 100);
                }
                fileOutputStream.write(buffer, 0, count);
                total += count;
            }
       
            fileOutputStream.close();
            inputStream.close();
            connection.close();
            
            if(!running){   //if the download was aborted, the file is deleted
                File f = new File(downloading.getName());
                f.delete();
            }
            
            hideFile();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(DownloadConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            NetworkManager.getInstance().removeDownloadConnection(this);
        }
    }
    
    private void updateDownloadStatus(int percentage){
        ListView downloads = DisplayedData.getInstance().getDownloadingFiles();
        Runnable run = null;
        
        index= 0;
        
        if(!fileVisible){
            run = new Runnable(){
                @Override
            public void run(){
                downloads.getItems().add(downloading+" (0%)");
            }
            };
            
            Platform.runLater(run);
            fileVisible = true;
            
            return;
        }
        
        for(int i = 0; i < downloads.getItems().size(); i++){
            if(Util.Utilities.removePercentage(downloads.getItems().get(i).toString()).compareTo(downloading.toString()) == 0) {
                index = i;
                break;
            } 
    }
        
        if(downloads != null){
            run = new Runnable(){
                @Override
            public void run(){
                int selectedIndex;
                selectedIndex = downloads.getSelectionModel().getSelectedIndex();
                downloads.getItems().set(index, downloading + " ("+percentage+"%)");
                downloads.getSelectionModel().select(selectedIndex);
            }
            };   
        }
        
        Platform.runLater(run);
    }
    
    public String getFileString(){
        return downloading.toString();
    }
    
    private void hideFile(){
         ListView downloads = DisplayedData.getInstance().getDownloadingFiles();
         
         Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (downloads != null && fileVisible == true) {
                    downloads.getItems().remove(index);
                    fileVisible = false;
                }
            }
        });
    }
}

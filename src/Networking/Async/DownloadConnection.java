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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    
    public DownloadConnection(OwnedFile downloading){
        this.downloading = downloading;
    }
    
    @Override
    public void run(){
        ConnectionResponse response;
        DownloadRequest request;
        byte[] buffer = new byte[2048];
        int count = 0;
        
        try {
            connection = new Socket(downloading.getOwner().getAddress(), 50101);
            
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
            
            NetworkManager.getInstance().addDownloadConnection(this);
            
            inputStream = connection.getInputStream();
            fileOutputStream = new FileOutputStream(downloading.getName());
            
            while(running && (count = inputStream.read(buffer)) > 0 ){
                updateDownloadStatus();
                fileOutputStream.write(buffer, 0, count);
            }
            
            fileOutputStream.close();
            inputStream.close();
            connection.close();
            
            hideFile();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(DownloadConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            NetworkManager.getInstance().removeDownloadConnection(this);
        }
    }
    
    private void updateDownloadStatus(){
        ListView downloads = DisplayedData.getInstance().getDownloadingFiles();
        
        if(downloads != null && fileVisible == false){
            downloads.getItems().add(downloading);
            fileVisible = true;
        }
    }
    
    private void hideFile(){
         ListView downloads = DisplayedData.getInstance().getDownloadingFiles();
         
         if(downloads != null && fileVisible == true){
             downloads.getItems().remove(downloading);
             fileVisible = false;
         }
    }
}

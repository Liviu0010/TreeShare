/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import Networking.NetworkManager;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class UploadConnectionListener extends NetworkThread{
    File toUpload;
    ServerSocket serverSocket;
    
    
    public UploadConnectionListener(int port, File toUpload){
        this.toUpload = toUpload;
        
        try {
            System.out.println("UploadConnectionListerner server port: "+port);
            serverSocket = new ServerSocket(port, 0);
        } catch (IOException ex) {
            Logger.getLogger(UploadConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        Socket connection;
        UploadConnection uploadConnection;
        
        try {
            serverSocket.setSoTimeout(60000);
            connection = serverSocket.accept();
            System.out.println("accepted connection");
            if(connection != null){
                uploadConnection = new UploadConnection(toUpload, connection);
                NetworkManager.getInstance().addUploadConnection(uploadConnection);
                uploadConnection.start();
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(UploadConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

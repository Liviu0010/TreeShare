/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import Networking.Messages.ConnectionResponse;
import Networking.NetworkManager;
import Networking.NetworkNode;
import Networking.PrivateRequests.DownloadRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class UploadListener extends NetworkThread{
    private static UploadListener instance;
    
    ServerSocket serverSocket;
    
    private UploadListener(){
        try {
            serverSocket = new ServerSocket(50101, 100);
        } catch (IOException ex) {
            Logger.getLogger(UploadListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static UploadListener getInstance(){
        if(instance == null){
            instance = new UploadListener();
        }
        
        return instance;
    }
    
    public void startListening(){
        instance.start();
    }
    
    @Override
    public void run(){
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        Socket current;
        DownloadRequest message;
        ConnectionResponse response;
        UploadConnectionListener uploadConnectionListener;
        int port;
        
        while(running){
            try {
                current = serverSocket.accept();
                
                current.setSoTimeout(10000);
                
                objectOutputStream = new ObjectOutputStream(current.getOutputStream());
                objectInputStream = new ObjectInputStream(current.getInputStream());
                
                message = (DownloadRequest) objectInputStream.readObject();
                
                try{
                    port = NetworkManager.getInstance().reserveUploadPort();
                    response = new ConnectionResponse(port);
                    response.addVisited(new NetworkNode(port));
                    uploadConnectionListener = new UploadConnectionListener(port, message.getFile());
                    uploadConnectionListener.start();
                    
                    objectOutputStream.writeObject(response);
                }
                catch(ClassCastException ex){
                    Logger.getLogger(UploadListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                current.close();
                
            } catch (IOException | ClassNotFoundException ex) {
                if(running == true)
                    Logger.getLogger(UploadListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Override
    public void stopRunning(){
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(UploadListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

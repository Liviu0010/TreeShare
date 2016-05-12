/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import Networking.Messages.ConnectionRequest;
import Networking.Messages.ConnectionResponse;
import Networking.Messages.Message;
import Networking.NetworkManager;
import Networking.NetworkNode;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class ConnectionListener extends NetworkThread{
    ServerSocket serverSocket;
    
    public ConnectionListener(){
        try {
            serverSocket = new ServerSocket(50000, 100);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        Socket current;
        Message msg;
        int port;
        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream;
        ConnectionResponse response;
        Listener listener;
        
        while(running){
            try {
                current = serverSocket.accept();
                
                current.setSoTimeout(10000);
                objectInputStream = new ObjectInputStream(current.getInputStream());
                msg = (Message) objectInputStream.readObject();
                objectInputStream.close();
                
                if(msg instanceof ConnectionRequest){
                    port = NetworkManager.getInstance().reservePort();
                    response = new ConnectionResponse(port);
                    response.addVisited(new NetworkNode(port));
                    
                    //listens for incoming connections on that port
                    listener = new Listener(port);
                    listener.start();
                    //
                    
                    objectOutputStream = new ObjectOutputStream(current.getOutputStream());
                    objectOutputStream.writeObject(response);
                    objectOutputStream.close();
                }
                
                current.close();
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

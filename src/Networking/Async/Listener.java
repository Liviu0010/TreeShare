/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import Networking.NetworkManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class Listener extends NetworkThread{
    int port;
    ServerSocket serverSocket;
    
    public Listener(int port){
        try {
            serverSocket = new ServerSocket(port, 0);
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        Socket socket;
        Connection connection;
        
        try {
            serverSocket.setSoTimeout(60000);   //only waits for 60 seconds
            socket = serverSocket.accept();
            
            if(socket != null){
                connection = new Connection(socket);
                NetworkManager.getInstance().addConnection(connection);
                connection.start();
            }
            
            serverSocket.close();
        } catch (SocketException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

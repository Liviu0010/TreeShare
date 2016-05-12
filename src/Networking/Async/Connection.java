/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import Networking.Messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class Connection extends NetworkThread{
    Socket connection;
    
    public Connection(Socket connectionSocket){
        connection = connectionSocket;
    }
    
    @Override
    public void run(){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(connection.getInputStream());
            Message msg;
            
            while(running){
                msg = (Message) objectInputStream.readObject();
                
                System.out.println(msg.getVisited().get(0).getAddress());
                //TODO implement behaviour
            }
            
            objectInputStream.close();
            connection.close();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Socket getSocket(){
        return connection;
    }
}

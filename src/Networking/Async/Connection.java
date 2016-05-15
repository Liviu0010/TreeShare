/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import FileSharing.FileSharingManager;
import Networking.Messages.Message;
import Networking.Messages.SearchQuery;
import Networking.Messages.SearchResult;
import Networking.NetworkManager;
import Networking.NetworkNode;
import Networking.OwnedFile;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class Connection extends NetworkThread{
    Socket connection;
    ObjectOutputStream objectOutputStream;
    byte[] MAC;
    
    public Connection(Socket connectionSocket, byte[] MAC){
        connection = connectionSocket;
        this.MAC = MAC;
    }
    
    @Override
    public void run(){
        try {
            objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(connection.getInputStream());
            Message msg;
            
            //TESTING
            System.out.println("Connection from "+connection.getInetAddress().getHostAddress()+" received on port " + connection.getLocalPort());
            //END
            
            while(running){
                msg = (Message) objectInputStream.readObject();
                
                System.out.println(msg.getVisited().get(0).getMACAddress()+":"+msg.getVisited().get(0).getPort());
                
                if(msg instanceof SearchQuery){
                    SearchQuery search = (SearchQuery)msg;
                    ArrayList<File> shared = FileSharingManager.getInstance().getSharedFiles();
                    ArrayList<OwnedFile> results = new ArrayList<>();
                    SearchResult result;
                    
                    for(int i = 0; i < shared.size(); i++){
                        if(shared.get(i).getName().toLowerCase().contains(search.getSearchString().toLowerCase()))
                            results.add(new OwnedFile(shared.get(i), new NetworkNode(InetAddress.getLocalHost().getHostAddress(), 
                            NetworkManager.getInstance().getMACAddress())));
                    }
                    
                    result = new SearchResult(results);
                    
                    search.addVisited(new NetworkNode(InetAddress.getLocalHost().getHostAddress(),
                        NetworkManager.getInstance().getMACAddress()));
                    
                    for(int i = 0; i < search.getVisited().size(); i++){
                        result.addVisited(search.getVisited().get(i));
                    }
                    
                    System.out.println("CONNECTION: sent search result");
                    Util.Utilities.displayMAC(System.out, search.getVisited().get(0).getMACAddress());
                    NetworkManager.getInstance().sendResponse(search);
                    NetworkManager.getInstance().sendResponse(result);
                }
                
                if(msg instanceof SearchResult){
                    System.out.println("CONNECTION: sent search result further");
                    NetworkManager.getInstance().sendResponse(msg);
                }
                
                //TODO implement behaviour
            }
            
            objectOutputStream.close();
            objectInputStream.close();
            connection.close();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Socket getSocket(){
        return connection;
    }
    
    public byte[] getMAC(){
        return MAC;
    }
    
    //to be called by outsiders
    public void sendMessage(Message message){
        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                synchronized(objectOutputStream){
                    try {
                        objectOutputStream.writeObject(message);
                    } catch (IOException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            }
        });
        
        t.start();
    }
    
    @Override
    public void stopRunning(){
        running = false;
        
        try {
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

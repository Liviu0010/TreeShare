/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import FileSharing.FileSharingManager;
import Networking.Messages.ConnectionRequest;
import Networking.Messages.ConnectionResponse;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class ParentConnection extends NetworkThread{
    private static ParentConnection instance;
    Socket connection;
    String address;
    int port;
    byte[] MAC;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    
    private ParentConnection(){
        
    }
    
    public static ParentConnection getInstance(){
        if(instance == null){
            instance = new ParentConnection();
        }
        
        return instance;
    }
    
    public void setParent(NetworkNode node){
        address = node.getAddress();
    }
    
    public boolean isReady(){
        return objectInputStream != null && objectOutputStream != null;
    }
    
    @Override
    public void run(){
        ConnectionRequest connectionRequest;
        ConnectionResponse connectionResponse;
        Message networkMessage = null;
        
        try {
            connection = new Socket(address, NetworkManager.getInstance().getNetworkConnectionListenerPort());
            connection.setSoTimeout(20000); //waits for 20 seconds
            
            objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
            objectInputStream = new ObjectInputStream(connection.getInputStream());
            
            connectionRequest = new ConnectionRequest(NetworkManager.getInstance().getMACAddress());
            
            objectOutputStream.writeObject(connectionRequest);
            
            connectionResponse = (ConnectionResponse) objectInputStream.readObject();
            
            if(connectionResponse.getAssignedPort() == -1)
                return;
            
            this.MAC = connectionResponse.getMAC();
            
            objectOutputStream.close();
            objectInputStream.close();
            connection.close();
            
            objectOutputStream = null;
            objectInputStream = null;
            
            connection = new Socket(address, connectionResponse.getAssignedPort());
            
            objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
            objectInputStream = new ObjectInputStream(connection.getInputStream());
            
            System.out.println("Connected to "+connection.getInetAddress().getHostAddress()+" on port "+connection.getPort());
            
            while(running){
                try{
                networkMessage = (Message) objectInputStream.readObject();
                }
                catch(SocketException ex){
                    if(running){
                        Logger.getLogger(ParentConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                
                if(networkMessage instanceof SearchQuery){
                    SearchQuery search = (SearchQuery)networkMessage;
                    ArrayList<File> shared = FileSharingManager.getInstance().getSharedFiles();
                    ArrayList<OwnedFile> results = new ArrayList<>();
                    SearchResult result;
                    
                    for(int i = 0; i < shared.size(); i++){
                        if(shared.get(i).getName().toLowerCase().contains(search.getSearchString().toLowerCase()))
                            results.add(new OwnedFile(shared.get(i), new NetworkNode(InetAddress.getLocalHost().getHostAddress())));
                    }
                    
                    result = new SearchResult(results);
                    search.addVisited(new NetworkNode(InetAddress.getLocalHost().getHostAddress(), 
                            NetworkManager.getInstance().getMACAddress()));
                    
                    
                    
                    for(int i = 0; i < search.getVisited().size(); i++){
                        result.addVisited(search.getVisited().get(i));
                    }
                    
                    System.out.println("sent query");
                    
                    NetworkManager.getInstance().sendResponse(search);
                    NetworkManager.getInstance().sendResponse(result);
                }
                
                if(networkMessage instanceof SearchResult){
                    
                    System.out.println("received result");
                    
                    NetworkManager.getInstance().sendResponse(networkMessage);
                }
                //TODO implement behaviour
            }
            
            objectOutputStream.close();
            objectInputStream.close();
            connection.close();
            
            objectOutputStream = null;
            objectInputStream = null;
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ParentConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getAddress(){
        return address;
    }
    
    public int getPort(){
        return port;
    }
    
    public byte[] getMAC(){
        return MAC;
    }
    
    
    //to be called by outsiders
    public void sendMessage(Message message){
        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    synchronized(objectOutputStream){
                        objectOutputStream.writeObject(message);
                        System.out.println("PARENT CONNECTION: sent message");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ParentConnection.class.getName()).log(Level.SEVERE, null, ex);
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

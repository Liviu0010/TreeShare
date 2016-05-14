/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;

import Interface.DisplayedData;
import Networking.Async.UploadConnection;
import Networking.Async.Connection;
import Networking.Async.ConnectionListener;
import Networking.Async.DownloadConnection;
import Networking.Async.ParentConnection;
import Networking.Async.UploadListener;
import Networking.Messages.Message;
import Networking.Messages.SearchQuery;
import Networking.Messages.SearchResult;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 *
 * @author Liviu
 */
public class NetworkManager {
    private static NetworkManager instance;
    private final ArrayList<Connection> child;
    private ConnectionListener connectionListener;
    private boolean[] ports;
    private boolean[] uploadPorts;
    private ArrayList<DownloadConnection> downloadConnections;
    private ArrayList<UploadConnection> uploadConnections;
    private String currentSearchQuery;
    private boolean shutdownCalled;
    
    private NetworkManager(){
        child = new ArrayList<>();
        downloadConnections = new ArrayList<>();
        uploadConnections = new ArrayList<>();
        connectionListener = new ConnectionListener();
        ports = new boolean[100];
        uploadPorts = new boolean[100];
        shutdownCalled = false;
    }
    
    public static NetworkManager getInstance(){
        if(instance == null)
            instance = new NetworkManager();
        
        return instance;
    }
    
    public int reservePort(){
        int port = -1;
        
        for(int i = 0; i < 100; i++){
            if(ports[i] == false){
                port = i + 50001;
                i = 100;
            }
        }
        
        return port;
    }
    
    public int reserveUploadPort(){
        int port = -1;
        
        for(int i = 0; i < 100; i++){
            if(uploadPorts[i] == false){
                uploadPorts[i] = true;
                port = i + 50102;
                i = 100;
            }
        }
        
        return port;
    }
    
    public void freePort(int port){
        if(port >= 0 && port < 100)
            ports[port] = false;
    }
    
    public void freeUploadPort(int port){
        if(port >= 0 && port < 100){
            uploadPorts[port] = false;
        }
    }
    
    public void startListening(){
        connectionListener.start();
    }
    
    public void stopListening(){
        connectionListener.stopRunning();
        
        for(int i = 0; i < 100; i++){
            ports[i] = false;
        }
    }
    
    public void addConnection(Connection connection){
        synchronized(child){
            child.add(connection);
        }
    }
    
    public void addDownloadConnection(DownloadConnection downloadConnection){
        synchronized(downloadConnections){
            downloadConnections.add(downloadConnection);
        }
    }
    
    public void addUploadConnection(UploadConnection toAdd){
        synchronized(uploadConnections){
            uploadConnections.add(toAdd);
        }
    }
    
    public void removeDownloadConnection(DownloadConnection toRemove){
        downloadConnections.remove(toRemove);
    }
    
    public void removeUploadConnection(UploadConnection toRemove){
        uploadConnections.remove(toRemove);
        freeUploadPort(toRemove.getPort());
    }
    
    public void search(String searchQuery){
        currentSearchQuery = searchQuery;
        SearchQuery search = new SearchQuery(searchQuery);
        
        try {
            search.addVisited(new NetworkNode(InetAddress.getLocalHost().getHostAddress()));
            
            if(ParentConnection.getInstance().isReady()){
                ParentConnection.getInstance().sendMessage(search);
                System.out.println("NETWORK MANAGER: searched for "+searchQuery+" on parentConnection");
            }
            
            for(int i = 0; i < child.size(); i++){
                 child.get(i).sendMessage(search);
        }
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendResponse(Message message){
        if(message instanceof SearchResult){
            try {
                SearchResult result = (SearchResult)message;
                int nextInd = result.getVisited().size()-1;
                NetworkNode nextNode = null;
                 System.out.println("nextInd = "+nextInd);
                 System.out.println("size of visited = "+result.getVisited().size());
                if(nextInd > 0){
                    if(result.getVisited().size() > 1)
                        result.getVisited().remove(nextInd);
                    nextNode = result.getVisited().get(result.getVisited().size()-1);
                }
                else if(result.getVisited().get(0).getAddress().compareTo(InetAddress.getLocalHost().getHostAddress()) == 0){
                    
                    
                    addToSearchList(result);
                    return;
                }
                else
                    return;
                
                System.out.println(InetAddress.getLocalHost().getHostAddress()+" --- "+result.getVisited().get(0).getAddress());
                
                if(ParentConnection.getInstance().isReady() && ParentConnection.getInstance().getAddress().compareTo(nextNode.getAddress()) == 0){
                    ParentConnection.getInstance().sendMessage(result);
                }
                
                for(int i = 0; i < child.size(); i++){
                    System.out.println(nextNode.getAddress()+" --- "+child.get(i).getSocket().getInetAddress().getHostAddress());
                    if(nextNode.getAddress().compareTo(child.get(i).getSocket().getInetAddress().getHostAddress()) == 0){
                        child.get(i).sendMessage(result);
                        System.out.println("NETWORK MANAGER: SENT response to child");
                    }
                }
                
            } catch (UnknownHostException ex) {
                Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void addToSearchList(SearchResult result){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                boolean updated = false;
                ListView downloads;
                
                while(!updated && !shutdownCalled){
                    downloads = DisplayedData.getInstance().getSearchResults();
                    
                    if(downloads != null){
                        updated = true;
                        List<OwnedFile> results = result.getFiles();
                        
                        for(int i =0; i < results.size(); i++){
                            System.out.println(results.get(i).toString());
                            downloads.getItems().add(results.get(i));
                        }
                        
                    }
                    
                }
            }
        });
    }
    
    public void downloadFile(OwnedFile toDownload){
        DownloadConnection download = new DownloadConnection(toDownload);
        downloadConnections.add(download);
        download.start();
    }
    
    public void stopDownload(String fileName){
        
        for(int i = 0; i < downloadConnections.size(); i++){
            if(downloadConnections.get(i).getFileString().compareTo(Util.Utilities.removePercentage(fileName)) == 0){
                downloadConnections.get(i).stopRunning();
                downloadConnections.remove(i);
                break;
            }
        }
    }
    
    public void connectTo(NetworkNode target){
        ParentConnection.getInstance().setParent(target);
        ParentConnection.getInstance().start();
    }
    
    public boolean shutdownCalled(){
        return shutdownCalled;
    }
    
    public void stopAll(){
        UploadListener.getInstance().stopRunning();
        
        if(ParentConnection.getInstance().isReady())
            ParentConnection.getInstance().stopRunning();
        
        if(connectionListener != null)
            connectionListener.stopRunning();
        
        for(int i = 0; i<child.size(); i++)
            child.get(i).stopRunning();
        
        for(int i = 0; i<uploadConnections.size(); i++){
            uploadConnections.get(i).stopRunning();
        }
        
        for(int i = 0; i<downloadConnections.size(); i++){
            downloadConnections.get(i).stopRunning();
        }
    }
    
}

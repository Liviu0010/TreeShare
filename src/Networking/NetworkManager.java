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
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
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
    private int networkConnectionListenerPort, networkMessagesPort, downloadRequestListenerPort, fileTransferPort;
    private ArrayList<DownloadConnection> downloadConnections;
    private ArrayList<UploadConnection> uploadConnections;
    private String currentSearchQuery;
    private boolean shutdownCalled;
    
    private NetworkManager(){
        child = new ArrayList<>();
        downloadConnections = new ArrayList<>();
        uploadConnections = new ArrayList<>();
        shutdownCalled = false;
        
        /*networkConnectionListenerPort = 50000;
        networkMessagesPort = 50001;
        downloadRequestListenerPort = 50002;
        fileTransferPort = 50003;*/
    }
    
    public static NetworkManager getInstance(){
        if(instance == null)
            instance = new NetworkManager();
        
        return instance;
    }
    
    public void setNetworkConnectionListenerPort(int port){
        networkConnectionListenerPort = port;
    }
    
    public void setNetworkMessagesPort(int port){
        networkMessagesPort = port;
    }
    
    public void setDownloadRequestListenerPort(int port){
        downloadRequestListenerPort = port;
    }
    
    public void setFileTransferPort(int port){
        fileTransferPort = port;
    }
    
    public int getNetworkConnectionListenerPort(){
        return networkConnectionListenerPort;
    }
    
    public int getDownloadRequestListenerPort(){
        return downloadRequestListenerPort;
    }
     
    public int getNetworkMessagesPort(){
        return networkMessagesPort;
    }
    
    public int getFileTransferPort(){
        return fileTransferPort;
    }
    
    
    public void startListening(){
        connectionListener = new ConnectionListener();
        connectionListener.start();
        UploadListener.getInstance().start();
    }
    
    public void stopListening(){
        connectionListener.stopRunning();
        UploadListener.getInstance().stopRunning();
        
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
        //freeUploadPort(toRemove.getPort());
    }
    
    public void search(String searchQuery){
        currentSearchQuery = searchQuery;
        SearchQuery search = new SearchQuery(searchQuery);
        
        try {
            search.addVisited(new NetworkNode(InetAddress.getLocalHost().getHostAddress(), 
                    getMACAddress()));
            
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
                System.out.println("--sendResponse SearchResult--");
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
                //else if(result.getVisited().get(0).getAddress().compareTo(InetAddress.getLocalHost().getHostAddress()) == 0){
                else if(Util.Utilities.sameMAC(result.getVisited().get(0).getMACAddress(), 
                        NetworkManager.getInstance().getMACAddress())){
                    
                    addToSearchList(result);
                    return;
                }
                else
                    return;
                
                System.out.println(InetAddress.getLocalHost().getHostAddress()+" --- "+result.getVisited().get(0).getAddress());
                
                if(ParentConnection.getInstance().isReady() && Util.Utilities.sameMAC(ParentConnection.getInstance().getMAC(), nextNode.getMACAddress())){
                    ParentConnection.getInstance().sendMessage(result);
                }
                
                for(int i = 0; i < child.size(); i++){
                    System.out.println(nextNode.getAddress()+" --- "+child.get(i).getSocket().getInetAddress().getHostAddress());
                    //print MAC
                    for(int k = 0; k < nextNode.getMACAddress().length; k++)
                        System.out.print(nextNode.getMACAddress()[k]+" ");
                    System.out.println("--- ");
                    for(int k = 0; k < child.get(i).getMAC().length; k++)
                        System.out.print(child.get(i).getMAC()[k]+" ");
                    System.out.println();
                   
                    //END
                    
                    // if(Util.Utilities.sameMAC(child.get(i).getMAC(), result.getVisited().get(result.getVisited().size()-1).getMACAddress())){
                   {
                        child.get(i).sendMessage(result);
                        System.out.println("NETWORK MANAGER: SENT response to child");
                    }
                }
                
            } catch (UnknownHostException ex) {
                Logger.getLogger(NetworkManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(message instanceof SearchQuery){
            SearchQuery search = (SearchQuery) message;
            
            if(ParentConnection.getInstance().isReady() && !Util.Utilities.visited(new NetworkNode(ParentConnection.getInstance().getAddress(), ParentConnection.getInstance().getMAC()), search.getVisited())){
                ParentConnection.getInstance().sendMessage(search);
            }
            
            for(int i = 0; i<child.size(); i++){
                if(!Util.Utilities.visited(new NetworkNode(child.get(i).getSocket().getInetAddress().getHostAddress(), child.get(i).getMAC()), search.getVisited()))
                    child.get(i).sendMessage(search);
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
    
    
    public byte[] getMACAddress() {
        byte[] MAC = null;
        
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface current;

            while (networkInterfaces.hasMoreElements()) {
                current = networkInterfaces.nextElement();
                MAC = current.getHardwareAddress();
                
                if (MAC != null && (MAC[MAC.length-1]) != -32) {
                    break;
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return MAC;
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
    
    public void removeConnction(Connection toRemove){
        child.remove(toRemove);
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

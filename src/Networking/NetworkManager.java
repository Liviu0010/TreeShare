/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;

import Networking.Async.Connection;
import Networking.Async.ConnectionListener;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Liviu
 */
public class NetworkManager {
    private static NetworkManager instance;
    private final ArrayList<Connection> child;
    private Connection parent;
    private ConnectionListener connectionListener;
    private boolean[] ports;
    
    private NetworkManager(){
        child = new ArrayList<>();
        connectionListener = new ConnectionListener();
        ports = new boolean[100];
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
    
    public void freePort(int port){
        if(port >= 0 && port < 100)
            ports[port] = false;
    }
    
    public void startListening(){
        connectionListener.start();
    }
    
    public void stopListening(){
        connectionListener.stopRunning();
    }
    
    public void addConnection(Connection connection){
        synchronized(child){
            child.add(connection);
        }
    }
    
    public void connectTo(NetworkNode target){
        //Not implemented
    }
}

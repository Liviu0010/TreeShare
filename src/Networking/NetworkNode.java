/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking;

/**
 *
 * @author Liviu
 */
public class NetworkNode extends NetworkObject{
    private final String address;
    private final int port;
    
    public NetworkNode(String address, int port) {
        this.address = address;
        this.port = port;
    }
    
    public NetworkNode(int port){
        this.port = port;
        address = null;
    }
    
    public NetworkNode(String address){
        this.port = -1;
        this.address = address;
    }
    
    public int getPort(){
        return port;
    }
    
    public String getAddress(){
        return address;
    }
}

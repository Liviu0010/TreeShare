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
    private String address;
    private int port = -1;
    private byte[] MAC;
    
    public NetworkNode(String address, byte[] MAC){
        this(address);
        this.MAC = MAC;
    }
    
    public NetworkNode(String address, int port, byte[] MAC){
        this(address, port);
        this.MAC = MAC;
    }
    
    public NetworkNode(String address, int port) {
        this(address);
        this.port = port;
    }
    
    public NetworkNode(int port){
        this.port = port;
    }
    
    public NetworkNode(String address){
        this.address = address;
    }
    
    public int getPort(){
        return port;
    }
    
    public String getAddress(){
        return address;
    }
    
    public byte[] getMACAddress(){
        return MAC;
    }
}

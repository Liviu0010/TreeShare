/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Messages;

/**
 *
 * @author Liviu
 */
public class ConnectionResponse extends Message{
    private int assignedPort;
    private byte[] MAC;
    
    public ConnectionResponse(int assignedPort, byte[] MAC){
        this.assignedPort = assignedPort;
        this.MAC = MAC;
    }
    
    public int getAssignedPort(){
        return assignedPort;
    }
    
    public byte[] getMAC(){
        return MAC;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Messages;

import Networking.NetworkNode;

/**
 *
 * @author Liviu
 */
public class RemoveNodeRequest extends Message{
    NetworkNode toRemove;
    
    public RemoveNodeRequest(NetworkNode toRemove){
        this.toRemove = toRemove;
    }
    
    public NetworkNode getNode(){
        return toRemove;
    }
}

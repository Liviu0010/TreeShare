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
public class AddNodeRequest extends Message{
    NetworkNode toAdd;
    
    public AddNodeRequest(NetworkNode toAdd){
        this.toAdd = toAdd;
    }
    
    public NetworkNode getNode(){
        return toAdd;
    }
}

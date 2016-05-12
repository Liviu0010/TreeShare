/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Messages;

import Networking.NetworkNode;
import java.util.ArrayList;

/**
 *
 * @author Liviu
 */
public class NetworkNodesResponse extends Message{
    ArrayList<NetworkNode> nodeList;
    
    public NetworkNodesResponse(ArrayList<NetworkNode> nodeList){
        this.nodeList = nodeList;
    }
    
    public ArrayList<NetworkNode> getNodeList(){
        return nodeList;
    }
}

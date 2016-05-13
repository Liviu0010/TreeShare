/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Messages;

import Networking.NetworkNode;
import Networking.NetworkObject;
import java.util.ArrayList;

/**
 *
 * @author Liviu
 */
public class Message extends NetworkObject {
    protected ArrayList<NetworkNode> visited;
    
    public Message(){
        visited = new ArrayList<>();
    }
    
    public void addVisited(NetworkNode visitedNode){
        visited.add(visitedNode);
    }
    
    public ArrayList<NetworkNode> getVisited(){
        return visited;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Messages;

import Networking.NetworkNode;
import Networking.NetworkObject;
import Networking.OwnedFile;
import java.util.ArrayList;

/**
 *
 * @author Liviu
 */
public class SearchResult extends Message{
    private final ArrayList<OwnedFile> filesFound;
    
    public SearchResult(ArrayList<OwnedFile> found){
        filesFound = found;
    }
    
    public NetworkNode getNode(){
        if(filesFound.size() > 0)
            return filesFound.get(0).getOwner();
        else
            return null;
    }
    
    public ArrayList<OwnedFile> getFiles(){
        return filesFound;
    }
}

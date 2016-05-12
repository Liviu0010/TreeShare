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

import java.io.File;
import java.net.URI;

public class OwnedFile extends File{
    private final NetworkNode owner;
    
    public OwnedFile(String pathname) {
        super(pathname);
        this.owner = null;
    }
    
    public OwnedFile(File parent, String child){
        super(parent, child);
        this.owner = null;
    }
    
    public OwnedFile(String parent, String child){
        super(parent, child);
        this.owner = null;
    }
    
    public OwnedFile(URI uri){
        super(uri);
        this.owner = null;
    }
    
    public OwnedFile(File normalFile, NetworkNode owner){
        super(normalFile.toString());
        this.owner = owner;
    }
    
    public NetworkNode getOwner(){
        return owner;
    }
    
}

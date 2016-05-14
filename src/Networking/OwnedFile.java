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
    private long size;
    
    public OwnedFile(String pathname) {
        super(pathname);
        this.owner = null;
        this.size = this.length();
    }
    
    public OwnedFile(File parent, String child){
        super(parent, child);
        this.owner = null;
        this.size = this.length();
    }
    
    public OwnedFile(String parent, String child){
        super(parent, child);
        this.owner = null;
        this.size = this.length();
    }
    
    public OwnedFile(URI uri){
        super(uri);
        this.owner = null;
        this.size = this.length();
    }
    
    public OwnedFile(File normalFile, NetworkNode owner){
        super(normalFile.toString());
        this.owner = owner;
        this.size = this.length();
    }
    
    public NetworkNode getOwner(){
        return owner;
    }
    
    public long size(){
        return size;
    }
    
}

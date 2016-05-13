/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.PrivateRequests;

import Networking.NetworkObject;
import java.io.File;

/**
 *
 * @author Liviu
 */
public class DownloadRequest extends NetworkObject{
    private File toDownload;
    
    public DownloadRequest(File toDownload){
        this.toDownload = toDownload;
    }
    
    public File getFile(){
        return toDownload;
    }
}

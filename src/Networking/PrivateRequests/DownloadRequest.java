/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.PrivateRequests;

import Networking.NetworkObject;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Liviu
 */
public class DownloadRequest extends NetworkObject{
    private ArrayList<File> toDownload;
    
    public DownloadRequest(ArrayList<File> toDownload){
        for(int i = 0; i < toDownload.size(); i++){
            this.toDownload.add(toDownload.get(i));
        }
    }
}

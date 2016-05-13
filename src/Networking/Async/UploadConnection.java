/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liviu
 */
public class UploadConnection extends NetworkThread{
    File toUpload;
    Socket connection;
    OutputStream outputStream;
    FileInputStream fis;
    
    public UploadConnection(File toUpload, Socket connection) {
        this.toUpload = toUpload;
        this.connection = connection;
    }
    
    @Override
    public void run(){
        byte[] buffer = new byte[2048];
        int count;
        int total = 0;
        
        try {
            System.out.println("entered upload thread");
            outputStream = connection.getOutputStream();
            fis = new FileInputStream(toUpload);
            System.out.println("Got output stream for upload");
            while(running && (count = fis.read(buffer)) > 0){
                total += count;
                System.out.println("Uploaded "+total+" bytes.");
                outputStream.write(buffer, 0, count);
            }
            
            fis.close();
            outputStream.close();
            connection.close();
        
        } catch (IOException ex) {
            Logger.getLogger(UploadConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getPort(){
        return connection.getLocalPort();
    }
}

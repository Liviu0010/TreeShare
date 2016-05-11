/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FileSharing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 *
 * @author Liviu
 */
public class FileSharingManager {
    private static FileSharingManager instance;
    private final String PATH = "sharedFiles.txt";
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private ArrayList<File> sharedFiles;
    
    private FileSharingManager(){
        sharedFiles = new ArrayList<>();
        readFiles();
    }
    
    private void readFiles(){
        ArrayList<File> temp;
        
        try {
            fileInputStream = new FileInputStream(PATH);
            objectInputStream = new ObjectInputStream(fileInputStream);
            temp = (ArrayList<File>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            copyElements(temp);
            
        } catch (FileNotFoundException ex) {
            try {
                File f = new File(PATH);
                f.createNewFile();
                writeFiles();
            } catch (IOException ex1) {
                System.err.println("IOException! Don't have writing rights!");
            }
        } catch (IOException ex) {
            System.err.println("IOException! Reading error! "+ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("ClassNotFoundException! The share index file may be corrupted!");
        }
    }
    
    private void writeFiles(){
        try {
            fileOutputStream = new FileOutputStream(PATH);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(sharedFiles);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException ex) {
            System.err.println("FILE NOT FOUND!");
        } catch (IOException ex) {
            System.err.println("IOException when trying to write!");
        }
        
    }
    
    private void copyElements(ArrayList<File> source){
        sharedFiles.clear();
        for(int i = 0; i < source.size(); i++)
            sharedFiles.add(source.get(i));
    }
    
    public void addFiles(ArrayList<File> toAdd){
        for(int i = 0; i < toAdd.size(); i++)
            sharedFiles.add(toAdd.get(i));
        
        writeFiles();
    }
    
    public void removeFiles(ArrayList<File> toRemove){
        for(int i = 0; i < toRemove.size(); i++){
            for(int j = 0; j < sharedFiles.size(); j++)
                if(toRemove.get(i).compareTo(sharedFiles.get(j)) == 0)
                    sharedFiles.remove(j);
        }
        
        writeFiles();
    }
    
    public ArrayList<File> getSharedFiles(){
        return sharedFiles;
    }
    
    public static FileSharingManager getInstance(){
        if(instance == null){
            instance = new FileSharingManager();
        }
        
        return instance;
    }
}

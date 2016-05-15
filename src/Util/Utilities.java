/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Networking.NetworkManager;
import Networking.NetworkNode;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author Liviu
 */
public class Utilities {
    
    /**
     * Removes the percentage from a path like
     * "path to file (percentage%)"
     * @param pathWithPercentage
     * @return path without percentage
     */
    public static String removePercentage(String pathWithPercentage){
        String result = "";
        String[] arr = pathWithPercentage.split(" ");
        
        for(int i = 0; i<arr.length-1; i++){
            result += arr[i];
            
            if(i < arr.length-2)
                result += " ";
        }
        
        return result;
    }
    
    public static boolean visited(NetworkNode toCheck, ArrayList<NetworkNode> visited){
        for(int i = 0; i<visited.size(); i++){
            if(sameMAC(toCheck.getMACAddress(), visited.get(i).getMACAddress()))
                return true;
        }
        
        return false;
    }
    
    public static boolean sameMAC(byte[] MAC1, byte[] MAC2){
        for(int i = 0; i<MAC1.length; i++){
            if(MAC1[i] != MAC2[i])
                return false;
        }
        
        return true;
    }
    
    public static void displayMAC(PrintStream ps, byte[] MAC){
        if(MAC == null){
            ps.println("null");
            return;
        }
        
        for(int i = 0; i<MAC.length; i++){
            ps.print(MAC[i]);
            if(i < MAC.length - 1)
                ps.print(":");
        }
        
        ps.println();
    }
}

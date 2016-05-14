/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

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
}

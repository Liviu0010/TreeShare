/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Async;

/**
 *
 * @author Liviu
 */
public class NetworkThread extends Thread{
    boolean running;
    
    public NetworkThread(){
        running = true;
    }
    
    public void stopRunning(){
        running = false;
    }
}

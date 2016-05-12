/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Messages;

/**
 *
 * @author Liviu
 */
public class SearchQuery extends Message{
    private String searchString;
    
    public SearchQuery(String searchString){
        this.searchString = searchString;
    }
    
    public String getSearchString(){
        return searchString;
    }
}

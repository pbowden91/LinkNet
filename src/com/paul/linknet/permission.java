package com.paul.linknet; 
 
public class permission {
 
    //private variables
    int _user;
    String _name;
  //  int _receiver;
    String _file;
    int _expiration;
    int _id;
    
    // Empty constructor
    public permission(){
 
    }
    // constructor
    public permission(int user, String name, String file, int expiration){
    	this._user = user;
    	this._name = name;
        this._file = file;
        this._expiration = expiration;
    }

    public String getName(){
    	return this._name;
    }
    
    public void setName(String name){
    	this._name = name;
    }
    
    // getting ID
    public int getId(){
        return this._id;
    }
 
    // setting id
    public void setId(int id){
        this._id = id;
    }
    
    // getting ID
    public int getUser(){
        return this._user;
    }
 
    // setting id
    public void setUser(int id){
        this._user = id;
    }
////
//    // getting ID
//    public int getReceiver(){
//        return this._receiver;
//    }
// 
//    // setting id
//    public void setReceiver(int id){
//        this._receiver = id;
//    }

    // getting ID
    public String getFile(){
        return this._file;
    }
 
    // setting id
    public void setFile(String file){
        this._file = file;
    }

    // getting ID
    public int getExpiration(){
        return this._expiration;
    }
 
    // setting id
    public void setExpiration(int id){
        this._expiration = id;
    }
    
}
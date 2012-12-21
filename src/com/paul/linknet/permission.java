package com.paul.linknet; 
 
public class permission {
 
    //private variables
    int _host;
    int _receiver;
    String _file;
    int _expiration;
    int _id;
    
    // Empty constructor
    public permission(){
 
    }
    // constructor
    public permission(int id, int host, int receiver, String file, int expiration){
    	this._id = id;
        this._host = host;
        this._receiver = receiver;
        this._file = file;
        this._expiration = expiration;
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
    public int getHost(){
        return this._host;
    }
 
    // setting id
    public void setHost(int id){
        this._host = id;
    }

    // getting ID
    public int getReceiver(){
        return this._receiver;
    }
 
    // setting id
    public void setReceiver(int id){
        this._receiver = id;
    }

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
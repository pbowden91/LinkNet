package com.paul.linknet;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class sqllite extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;
 
    // Database Name
    private static final String DATABASE_NAME = "permissionsManager";
 
    // Contacts table name
    private static final String TABLE_PERMISSIONS = "permissions";

    // Contacts Table Columns names
    private static final String KEY_ID = "pid";
    private static final String KEY_TO = "username";
    private static final String KEY_FILE = "file";
    private static final String KEY_CREATED = "time_created";
    private static final String KEY_EXPIRATION = "time_to_expire";

    
    public sqllite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PERMISSIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TO + " TEXT," + KEY_FILE + " TEXT,"
                + KEY_CREATED + " TEXT," + KEY_EXPIRATION + " INTEGER " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        Log.d("DB create:", CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMISSIONS);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    void addPermission(permission p) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();

        String sql = "INSERT INTO " + TABLE_PERMISSIONS + " (" + KEY_TO + ", " + KEY_FILE + ", " + KEY_EXPIRATION + ") VALUES ('" + p.getName() + "', '" + p.getFile() + "', " + 0 + ")";
        
        // Inserting Row
        Log.d("Add: ", sql);
        db.execSQL(sql);
    }
 
    // Getting All Contacts
    public List<permission> getAllHostPermissions(int id) {
        List<permission> permissionList = new ArrayList<permission>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERMISSIONS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                permission p = new permission();
                p.setId(Integer.parseInt(cursor.getString(0)));
                p.setName((cursor.getString(1)));
                //p.setReceiver(Integer.parseInt(cursor.getString(2)));
                p.setFile(cursor.getString(2));
                p.setExpiration(Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                permissionList.add(p);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return permissionList;
    }
    // Getting All Contacts
    public List<permission> getAllPermissions() {
        List<permission> permissionList = new ArrayList<permission>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERMISSIONS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                permission p = new permission();
                Log.d("Name:", cursor.getString(1));
                p.setName((cursor.getString(1)));
                //p.setReceiver(Integer.parseInt(cursor.getString(2)));
                p.setFile(cursor.getString(2));
                p.setExpiration(0);
                // Adding contact to list
                permissionList.add(p);
           } while (cursor.moveToNext());
        }
 
        // return contact list
        return permissionList;    }
    
//    // Updating single contact
//    public int updateContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
// 
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
// 
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }
 
    // Deleting single contact
    public void deletePermission(permission p) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_PERMISSIONS + " WHERE " + KEY_TO + " = '" + p.getName() + "' AND " + KEY_FILE + " = '" + p.getFile() + "'";
        
        // Inserting Row
        Log.d("DELETE: ", sql);
        db.execSQL(sql);
        db.close();
    }
 
    // Getting contacts Count
    public int getContactsCount(int id) {
        String selectQuery = "SELECT  * FROM " + TABLE_PERMISSIONS + " WHERE " + KEY_TO + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    
}
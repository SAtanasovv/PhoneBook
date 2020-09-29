package com.satanasov.phonebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.satanasov.phonebook.Database;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;

import java.util.ArrayList;

import sqlligtmodel.Contact;
import sqlligtmodel.ContactQueries;

public class DataBaseHandler{
    public static final String     mDB_NAME = "contacts.db";

    private static DataBaseHandler mInstance;

    private Database               mDatabase;
    private Context                mContext;

    public static synchronized DataBaseHandler getInstance(Context context){
        if(null == mInstance){
            mInstance = new DataBaseHandler(context);
        }
        return mInstance;
    }

    private DataBaseHandler(Context context) {
        this.mContext=context;
        SqlDriver driver = new AndroidSqliteDriver(Database.Companion.getSchema(),mContext,mDB_NAME);
        mDatabase=Database.Companion.invoke(driver);
    }

    public void insertContact(String firstName, String lastName,String phoneNumbers){
        mDatabase.getContactQueries().InsertContact(firstName,lastName);
        mDatabase.getContactQueries().InsertEmail(phoneNumbers);
        //mDatabase.getContactQueries().InsertPhone(phoneNumbers);



    }

    public ArrayList printContacts(){
       ArrayList arrayList = (ArrayList) mDatabase.getContactQueries().SelectAll().executeAsList();
       return arrayList;
    }

    public void deleteContactById(int id){
        mDatabase.getContactQueries().DeleteContact(id);
    }



}

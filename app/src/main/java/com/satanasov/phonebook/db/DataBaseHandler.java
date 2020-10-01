package com.satanasov.phonebook.db;

import android.content.Context;

import com.satanasov.phonebook.Database;
import com.satanasov.phonebook.model.PhoneNumber;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;
import java.util.ArrayList;


public class DataBaseHandler {
    public static final String     mDB_NAME = "phoneBookContacts8.db";

    private static DataBaseHandler mInstance;

    private Database               mDatabase;
    private Context                mContext;

    private ArrayList<PhoneNumber> phoneNumbers;

    public Database getDatabase() {
        return mDatabase;
    }

    public static synchronized DataBaseHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new DataBaseHandler(context);
        }
        return mInstance;
    }

    private DataBaseHandler(Context context) {
        this.mContext=context;
        SqlDriver driver = new AndroidSqliteDriver(Database.Companion.getSchema(),mContext,mDB_NAME);
        mDatabase=Database.Companion.invoke(driver);
    }

    public ArrayList printContacts(){
       ArrayList arrayList = (ArrayList) mDatabase.getContactQueries().SelectAll().executeAsList();
       return arrayList;
    }

    public void deleteContactById(int id){
        mDatabase.getContactQueries().DeleteContact(id);
    }

}

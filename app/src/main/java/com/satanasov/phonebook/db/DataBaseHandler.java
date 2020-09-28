package com.satanasov.phonebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.satanasov.phonebook.Database;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;

import sqlligtmodel.Contact;
import sqlligtmodel.ContactQueries;

public class DataBaseHandler extends SQLiteOpenHelper {
    public static final String mDB_NAME = "contacts.db";
    public static final int mDB_VERSION = 1;

    private static DataBaseHandler mInstance;

    private Database               mDatabase;
    private Context                mContext;

    public static synchronized DataBaseHandler getInstance(Context context){
        if(null == mInstance){
            mInstance = new DataBaseHandler(context);
        }
        return mInstance;
    }

    private DataBaseHandler(@Nullable Context context) {
        super(context, mDB_NAME, null, mDB_VERSION);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SqlDriver driver = new AndroidSqliteDriver(Database.Companion.getSchema(),mContext,mDB_NAME);
        mDatabase=Database.Companion.invoke(driver);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertContact(String firstName, String lastName){
        mDatabase.getContactQueries().InsertContact(firstName,lastName);
    }

}

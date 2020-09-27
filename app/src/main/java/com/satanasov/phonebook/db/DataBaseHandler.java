package com.satanasov.phonebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.satanasov.phonebook.Database;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;

import sqlligtmodel.Contact;

public class DataBaseHandler extends SQLiteOpenHelper {
    public static final String DB_NAME = "contacts.db";
    public static final int DB_VERSION = 1;

    private static DataBaseHandler instance;

    public static DataBaseHandler getInstance(Context context){
        if(instance == null){
            instance = new DataBaseHandler(context);
        }
        return instance;
    }

    public DataBaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SqlDriver driver = new AndroidSqliteDriver(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

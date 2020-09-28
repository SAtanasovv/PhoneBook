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
    public static final String mDB_NAME = "contacts.db";
    public static final int mDB_VERSION = 1;

    private static DataBaseHandler mInstance;

    private Context                mContext;

    public static DataBaseHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new DataBaseHandler(context);
        }
        return mInstance;
    }

    public DataBaseHandler(@Nullable Context context) {
        super(context, mDB_NAME, null, mDB_VERSION);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SqlDriver driver = new AndroidSqliteDriver(Database.Schema,mContext,mDB_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

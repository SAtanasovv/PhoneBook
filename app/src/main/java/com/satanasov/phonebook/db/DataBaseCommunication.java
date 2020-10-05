package com.satanasov.phonebook.db;
import android.content.Context;
import com.satanasov.phonebook.Database;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;

public class DataBaseCommunication {
    public static final String           mDB_NAME = "phoneBookContacts8.db";
    private static DataBaseCommunication mInstance;
    private Database                     mDatabase;
    private Context                      mContext;

    public static synchronized DataBaseCommunication getInstance(Context context){
        if(mInstance == null){
            mInstance = new DataBaseCommunication(context);
        }
        return mInstance;
    }

    private DataBaseCommunication(Context context) {
        this.mContext=context;
        SqlDriver driver = new AndroidSqliteDriver(Database.Companion.getSchema(),mContext,mDB_NAME);
        this.mDatabase=Database.Companion.invoke(driver);
    }

    public Database getDatabase() {
        return mDatabase;
    }
}

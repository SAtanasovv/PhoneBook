package com.satanasov.phonebook.db;

import android.content.Context;

import com.satanasov.phonebook.Database;
import com.satanasov.phonebook.model.PhoneNumber;
import com.squareup.sqldelight.TransactionCallbacks;
import com.squareup.sqldelight.TransactionWithReturn;
import com.squareup.sqldelight.TransactionWithoutReturn;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;


public class DataBaseHandler implements TransactionWithReturn {
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

    public void insertContact(String firstName, String lastName,String email,ArrayList<PhoneNumber> phoneNumbers){

        //mDatabase.getContactQueries().InsertContact();
        //String result = mDatabase.getContactQueries().transactionWithResult(false,);
        mDatabase.getContactQueries().InsertUserName(firstName,lastName);
        mDatabase.getContactQueries().StoreID();
        mDatabase.getContactQueries().InsertEmail(email);
        for (int i = 0 ; i<phoneNumbers.size();i++){
        mDatabase.getContactQueries().InsertPhone(phoneNumbers.get(i).getPhoneNumber(),phoneNumbers.get(i).getPhoneNumberType());
        }
        //mDatabase.getContactQueries().Commit();
    }

    public ArrayList printContacts(){
       ArrayList arrayList = (ArrayList) mDatabase.getContactQueries().SelectAll().executeAsList();
       return arrayList;
    }

    public void deleteContactById(int id){
        mDatabase.getContactQueries().DeleteContact(id);
    }


    @NotNull
    @Override
    public Void rollback(Object o) {
        return null;
    }

    @Override
    public Object transaction(@NotNull Function1 function1) {
        return null;
    }

    @Override
    public void afterCommit(@NotNull Function0<Unit> function0) {

    }

    @Override
    public void afterRollback(@NotNull Function0<Unit> function0) {

    }
}

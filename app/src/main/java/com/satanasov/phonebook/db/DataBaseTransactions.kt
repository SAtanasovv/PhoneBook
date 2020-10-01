package com.satanasov.phonebook.db

import android.content.Context
import android.widget.Toast
import com.satanasov.phonebook.model.PhoneNumber
import java.util.ArrayList

fun insertIntoDB (firstName: String, lastName: String, email: String, phoneNumbers: ArrayList<PhoneNumber>,context: Context){
    val database =  DataBaseHandler.getInstance(context).database
        database.contactQueries.transaction {
        afterCommit { Toast.makeText(context,"Insert Success", Toast.LENGTH_SHORT).show() }
        afterRollback { Toast.makeText(context, "Insert Failed", Toast.LENGTH_SHORT).show() }
        database.contactQueries.InsertUserName(firstName,lastName)
        database.contactQueries.StoreID()
        database.contactQueries.InsertEmail(email)
        for (phoneNumber in phoneNumbers){
            database.contactQueries.InsertPhone(phoneNumber.phoneNumber,phoneNumber.phoneNumberType)
        }
    }
}
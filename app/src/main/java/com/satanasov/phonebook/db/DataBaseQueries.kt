package com.satanasov.phonebook.db

import android.content.Context
import android.widget.Toast
import com.satanasov.phonebook.R
import com.satanasov.phonebook.model.ContactPhoneNumber
import java.util.ArrayList

fun insertContactIntoDB (firstName: String, lastName: String, email: String, contactPhoneNumbers: ArrayList<ContactPhoneNumber>, context: Context){
    val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.transaction {
            afterCommit { Toast.makeText(context, R.string.insert_success, Toast.LENGTH_SHORT).show() }
            afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

            database.contactQueries.InsertUserName(firstName,lastName)
            database.contactQueries.StoreID()
            database.contactQueries.InsertEmail(email)
            for (phoneNumber in contactPhoneNumbers)
                database.contactQueries.InsertPhone(phoneNumber.phoneNumber,phoneNumber.phoneNumberType)
    }
}

fun showAllContactsFromDB(context: Context): List<Any>{
    var contactList : List<Any> = emptyList()
    val database                =  DataBaseCommunication.getInstance(context).database
        contactList = database.contactQueries.SelectAllContacts().executeAsList()

    return contactList
}

fun deleteContactByID(context: Context, id: Long){
    val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.DeleteContact(id)
}

fun updateUserName(context: Context, firstName: String, lastName: String, id: Long){
    val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.UpdateUserName(firstName,lastName,id)
}

fun updateEmail(context: Context, email: String, contactID: Long){
    val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.UpdateEmail(email,contactID)
}

fun updateContactNumber(context: Context, number: String, contactNumberID: Long){
    val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.UpdatePhoneNumber(number,contactNumberID)

}




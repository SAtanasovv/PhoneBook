package com.satanasov.phonebook.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.widget.Toast
import com.satanasov.phonebook.R
import com.satanasov.phonebook.model.ContactEmailModel
import com.satanasov.phonebook.model.ContactModel
import com.satanasov.phonebook.model.ContactPhoneNumberModel
import com.squareup.sqldelight.db.SqlCursor
import sqlligtmodel.ContactNumbers
import sqlligtmodel.GetAllContacts
import java.util.ArrayList

class DataBaseQueries {

    fun storeContact(context: Context,contact: ContactModel) {
        val database = DataBaseCommunication.getInstance(context).database
            database.contactQueries.transaction {
                afterCommit { Toast.makeText(context, R.string.insert_success, Toast.LENGTH_SHORT).show() }
                afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }
                // add contact photo contactQueries
                database.contactQueries.InsertUser(contact.firstName,contact.lastName)
                database.storeContactIDQueries.StoreID()
                for (phoneNumber in contact.contactPhoneNumberModels)
                    database.contactNumbersQueries.InsertPhone(phoneNumber.phoneNumber,phoneNumber.phoneNumberType)
                for (email in contact.contactEmailModels)
                    database.contactEmailQueries.InsertEmail(email.email,email.emailType)
            }
    }

        fun getAllContacts(context: Context): SqlCursor {
            var contactList: List<Any> = emptyList()
            val database = DataBaseCommunication.getInstance(context).database

            return database.contactQueries.GetAllContacts().execute()
        }

        fun getAllContactsAsList(context: Context): List<GetAllContacts> {
            var contactList: List<Any> = emptyList()
            val database = DataBaseCommunication.getInstance(context).database

            return database.contactQueries.GetAllContacts().executeAsList();
        }

        fun updateContact(context: Context,contact: ContactModel,number: ContactPhoneNumberModel,email: ContactEmailModel){
            val database = DataBaseCommunication.getInstance(context).database
                database.contactQueries.transaction {
                    afterCommit { Toast.makeText(context, R.string.insert_success, Toast.LENGTH_SHORT).show() }
                    afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

                    database.contactQueries.UpdateUserName(contact.firstName,contact.lastName,contact.id)
                    database.contactNumbersQueries.UpdatePhoneNumber(number.phoneNumber,number.id)
                    database.contactEmailQueries.UpdateEmail(email.email,email.id)
                }
        }

        fun deleteContactById(context: Context, id: Long){
            val database = DataBaseCommunication.getInstance(context).database
                database.contactQueries.transaction {
                    afterCommit { Toast.makeText(context, R.string.insert_success, Toast.LENGTH_SHORT).show() }
                    afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

                    database.contactEmailQueries.DeleteEmail(id)
                    database.contactNumbersQueries.DeletePhoneNumber(id)
                    database.contactQueries.DeleteContact(id)
                }
        }
}
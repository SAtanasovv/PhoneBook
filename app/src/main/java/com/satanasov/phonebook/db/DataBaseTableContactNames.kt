package com.satanasov.phonebook.db

import android.content.Context
import android.widget.Toast
import com.satanasov.phonebook.R
import com.satanasov.phonebook.model.ContactName

class DataBaseTableContactNames : DataBaseTables<ContactName>() {

    override fun add(t: ContactName, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.transaction {
            afterCommit { Toast.makeText(context, R.string.insert_success, Toast.LENGTH_SHORT).show() }
            afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

            database.contactQueries.InsertUserName(t.firstName,t.lastName)
            database.contactQueries.StoreID()
        }
    }

    override fun update(t: ContactName, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.UpdateUserName(t.firstName,t.lastName,t.id)
    }

    override fun delete(t: ContactName, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.DeleteContact(t.id)
    }


}
package com.satanasov.phonebook.db

import android.content.Context
import android.widget.Toast
import com.satanasov.phonebook.R

class DataBaseContactNames : DataBaseTables() {

    override fun <ContactNames> add(t : ContactNames, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.transaction {
            afterCommit { Toast.makeText(context, R.string.insert_success, Toast.LENGTH_SHORT).show() }
            afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

            //database.contactQueries.InsertUserName()
            database.contactQueries.StoreID()

        }
    }


    override fun update() {
        TODO("Not yet implemented")
    }

    override fun delete() {
        TODO("Not yet implemented")
    }
}
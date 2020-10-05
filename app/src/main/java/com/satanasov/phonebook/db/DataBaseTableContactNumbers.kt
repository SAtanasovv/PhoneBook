package com.satanasov.phonebook.db

import android.content.Context
import android.widget.Toast
import com.satanasov.phonebook.R
import com.satanasov.phonebook.model.ContactPhoneNumber

class DataBaseTableContactNumbers : DataBaseTables<ContactPhoneNumber>() {

    override fun add(t: ContactPhoneNumber, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.transaction {
            afterCommit { Toast.makeText(context, R.string.insert_success, Toast.LENGTH_SHORT).show() }
            afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

            for (phoneNumber in t.phoneNumbers)
                database.contactQueries.InsertPhone(phoneNumber.phoneNumber,phoneNumber.phoneNumberType)
        }
    }

    override fun update(t: ContactPhoneNumber, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.UpdatePhoneNumber(t.phoneNumber,t.contactID)
    }

    override fun delete(t: ContactPhoneNumber, context: Context) {

    }


}
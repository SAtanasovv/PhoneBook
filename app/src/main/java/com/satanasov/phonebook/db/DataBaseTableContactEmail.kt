package com.satanasov.phonebook.db

import android.content.Context
import com.satanasov.phonebook.model.ContactEmail

class DataBaseTableContactEmail : DataBaseTables<ContactEmail>() {

    override fun add(t: ContactEmail, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.InsertEmail(t.email)
    }

    override fun update(t: ContactEmail, context: Context) {
        val database =  DataBaseCommunication.getInstance(context).database
        database.contactQueries.UpdateEmail(t.email,t.contactID)
    }

    override fun delete(t: ContactEmail, context: Context) {

    }


}
package com.satanasov.phonebook.kotlinPhoneBook.db

import android.content.Context
import com.satanasov.phonebook.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver


object DataBaseCommunication {

    private const val mDB_NAME     = "phoneBookContacts8.db"

    fun getDataBase(context: Context): Database{
        val driver = AndroidSqliteDriver(Database.Schema, context, mDB_NAME)
        return Database.invoke(driver)
    }
}
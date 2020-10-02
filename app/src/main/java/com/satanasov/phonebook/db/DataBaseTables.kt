package com.satanasov.phonebook.db

import android.content.Context

abstract class DataBaseTables {

    abstract fun <T> add(t:T,context: Context)
    abstract fun update()
    abstract fun delete()
}
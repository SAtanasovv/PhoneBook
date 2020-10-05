package com.satanasov.phonebook.db

import android.content.Context

abstract class DataBaseTables<T> {

    abstract fun add(t: T, context: Context)
    abstract fun update(t: T, context: Context)
    abstract fun delete(t: T, context: Context)
}
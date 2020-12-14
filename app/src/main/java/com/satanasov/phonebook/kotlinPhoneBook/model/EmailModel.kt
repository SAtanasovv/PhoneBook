package com.satanasov.phonebook.kotlinPhoneBook.model

import java.io.Serializable

data class EmailModel(
        val id:              Long?   = null,
        val contactID:       Long?   = null,
        var email:           String? = null,
        var emailType:       Long?   = null,
        var dbOperationType: Int?    = null
): Serializable
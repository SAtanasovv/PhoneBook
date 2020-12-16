package com.satanasov.phonebook.kotlinPhoneBook.model

import java.io.Serializable

data class PhoneNumberModel(
        val id:              Long?   = null,
        val contactID:       Long?   = null,
        var phoneNumber:     String? = null,
        var phoneNumberType: Long?   = null,
        var dbOperationType: Int?    = null ): Serializable
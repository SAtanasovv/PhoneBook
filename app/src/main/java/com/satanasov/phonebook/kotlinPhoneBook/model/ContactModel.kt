package com.satanasov.phonebook.kotlinPhoneBook.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList
data class ContactModel(
        val id:                   Long?   = null,
        var imageResource:        Bitmap? = null,
        var firstName:            String? = null,
        var lastName:             String? = null,
        var expanded:             Boolean = false,
        var dataBaseContact:      Boolean = false,
        var dbOperationType:      Int?    = null,

        var phoneNumberModelList: ArrayList<PhoneNumberModel> = ArrayList(),
        var emailModelList:       ArrayList<EmailModel>       = ArrayList()
        ): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readParcelable(Bitmap::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readArrayList(PhoneNumberModel::class.java.classLoader) as ArrayList<PhoneNumberModel>,
            parcel.readArrayList(EmailModel::class.java.classLoader)       as ArrayList<EmailModel>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeValue(id)
            writeParcelable(imageResource, flags)
            writeString(firstName)
            writeString(lastName)
            writeByte(if (expanded) 1 else 0)
            writeByte(if (dataBaseContact) 1 else 0)
            writeValue(dbOperationType)
            writeList(phoneNumberModelList)
            writeList(emailModelList)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactModel> {
        override fun createFromParcel(parcel: Parcel): ContactModel {
            return ContactModel(parcel)
        }

        override fun newArray(size: Int): Array<ContactModel?> {
            return arrayOfNulls(size)
        }
    }
}
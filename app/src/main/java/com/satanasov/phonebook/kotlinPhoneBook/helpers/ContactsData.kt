package com.satanasov.phonebook.kotlinPhoneBook.helpers

import android.content.Context
import android.provider.ContactsContract
import com.satanasov.phonebook.R
import com.satanasov.phonebook.db.DataBaseQueries
import com.satanasov.phonebook.globalData.Utils
import com.satanasov.phonebook.helpers.BitmapUtils
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel
import com.satanasov.phonebook.kotlinPhoneBook.model.EmailModel
import com.satanasov.phonebook.kotlinPhoneBook.model.PhoneNumberModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class ContactsData(context: Context) {
    private val mContext: Context                 = context
    private val mDataBaseQueries: DataBaseQueries = DataBaseQueries()

    fun getContactModelListFromDataBase(): ArrayList<ContactModel>{
        var contactModel:        ContactModel
        var emailModel:          EmailModel
        var phoneNumberModel:    PhoneNumberModel
        val dataBaseContactList: ArrayList<ContactModel> = ArrayList()

        val cursorContact = mDataBaseQueries.getContacts(mContext)

        while (cursorContact.next()) {
            val emailList       = ArrayList<EmailModel>()
            val phoneNumberList = ArrayList<PhoneNumberModel>()
            contactModel = ContactModel(
                    id          = cursorContact.getLong(CONTACT_ID),
                    firstName   = cursorContact.getString(CONTACT_FIRST_NAME),
                    lastName    = cursorContact.getString(CONTACT_LAST_NAME))
            contactModel.dataBaseContact = true

            val cursorNumbers    = mDataBaseQueries.getContactPhoneNumbers(mContext, contactModel.id!!)
            while (cursorNumbers.next()) {
                phoneNumberModel = PhoneNumberModel(
                        id              = cursorNumbers.getLong(CONTACT_NUMBER_ID),
                        phoneNumber     = cursorNumbers.getString(CONTACT_NUMBER),
                        phoneNumberType = cursorNumbers.getLong(CONTACT_NUMBER_TYPE))
                phoneNumberList.add(phoneNumberModel)
            }
            contactModel.phoneNumberModelList = phoneNumberList

            val cursorEmails = mDataBaseQueries.getContactEmails(mContext, contactModel.id!!)
            while (cursorEmails.next()) {
                emailModel   = EmailModel(
                        id          = cursorEmails.getLong(CONTACT_EMAIL_ID),
                        email       = cursorEmails.getString(CONTACT_EMAIL),
                        emailType   = cursorEmails.getLong(CONTACT_EMAIL_TYPE))
                emailList.add(emailModel)
            }
            contactModel.emailModelList = emailList
            dataBaseContactList.add(contactModel)
        }
        return dataBaseContactList

    }

    fun getContactsModelListFromPhoneStorage(): ArrayList<ContactModel> {
        val mPhoneStorageContactList: ArrayList<ContactModel>
        val contactById = HashMap<Int, ContactModel>()
        val bitmapUtils = BitmapUtils()

        val projection  = arrayOf(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DATA15,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE
        )

        val selection     = ContactsContract.Data.MIMETYPE + " in (?, ?, ?)"

        val selectionArgs = arrayOf(
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
        )

        val uri             = ContactsContract.Data.CONTENT_URI

        val sortOrder       = ContactsContract.Contacts.DISPLAY_NAME + " ASC "

        val cursor          = mContext.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

        with(cursor!!){
        val mimeTypeIndex   = getColumnIndex(ContactsContract.Data.MIMETYPE)
        val idIndex         = getColumnIndex(ContactsContract.Data.CONTACT_ID)
        val nameIndex       = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val dataIndex       = getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA)
        val typeIndex       = getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE)
        val photoIndex      = getColumnIndex(ContactsContract.Data.DATA15)

        while (moveToNext()) {
            val contactId       = getInt(idIndex).toLong()
            val contactType     = getInt(typeIndex).toLong()
            val contactName     = getString(nameIndex)
            val contactData     = getString(dataIndex)
            val contactMimeType = getString(mimeTypeIndex)
            val contactPhoto    = getBlob(photoIndex)
            var contactModel    = contactById[contactId.toInt()]

            if (contactModel    == null)
                contactModel    = ContactModel(id = contactId, firstName = contactName)

            when (contactMimeType) {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> contactModel.emailModelList.add(EmailModel(email = contactData, emailType = contactType))
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> contactModel.phoneNumberModelList.add(PhoneNumberModel(phoneNumber = contactData, phoneNumberType = contactType))
                else -> contactModel.imageResource = bitmapUtils.getImageFromBytes(contactPhoto)
            }
            contactById[contactId.toInt()] = contactModel
            }
         cursor.close()
        }

        val contactModels: Collection<ContactModel> = contactById.values
        mPhoneStorageContactList                    = ArrayList(contactModels)

        return mPhoneStorageContactList
    }

    fun getSpinnerTypeText(typeID: Long): String? {
        var numberType: String? = ""
        when (typeID) {
            Utils.HOME_PHONE_NUMBER   -> numberType  = mContext.getString(R.string.type_home)
            Utils.WORK_PHONE_NUMBER   -> numberType  = mContext.getString(R.string.type_work)
            Utils.MOBILE_PHONE_NUMBER -> numberType  = mContext.getString(R.string.type_mobile)
            Utils.MAIN_PHONE_NUMBER   -> numberType  = mContext.getString(R.string.type_main)
        }
        return numberType
    }

    fun getSpinnerTypeID(type: String): Long? {
        var longID: Long? = 0L
        when (type) {
            mContext.getString(R.string.type_home)   -> longID = Utils.HOME_PHONE_NUMBER
            mContext.getString(R.string.type_work)   -> longID = Utils.WORK_PHONE_NUMBER
            mContext.getString(R.string.type_mobile) -> longID = Utils.MOBILE_PHONE_NUMBER
            mContext.getString(R.string.type_main)   -> longID = Utils.MAIN_PHONE_NUMBER
        }
        return longID
    }

    companion object{
        var CONTACT_ID          = 0
        var CONTACT_FIRST_NAME  = 1
        var CONTACT_LAST_NAME   = 2

        var CONTACT_NUMBER_ID   = 0
        var CONTACT_NUMBER      = 1
        var CONTACT_NUMBER_TYPE = 2

        var CONTACT_EMAIL_ID    = 0
        var CONTACT_EMAIL       = 1
        var CONTACT_EMAIL_TYPE  = 2
    }
}
package com.satanasov.phonebook.db

import android.content.Context
import android.widget.Toast
import com.satanasov.phonebook.R
import com.satanasov.phonebook.globalData.Utils
import com.satanasov.phonebook.kotlinPhoneBook.db.DataBaseCommunication
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel
import com.squareup.sqldelight.db.SqlCursor

class DataBaseQueries {

         fun storeContact(context: Context,contact: ContactModel) {
             val database = DataBaseCommunication.getDataBase(context)
                 database.contactQueries.transaction {

                     afterCommit { }
                     afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

                     database.contactQueries.InsertUser(contact.firstName,contact.lastName)
                     database.storeContactIDQueries.StoreID()

                     for (phoneNumber in contact.phoneNumberModelList)
                         database.contactNumbersQueries.InsertPhone(phoneNumber.phoneNumber,phoneNumber.phoneNumberType)

                     for (email in contact.emailModelList)
                         database.contactEmailQueries.InsertEmail(email.email,email.emailType)
                 }
         }

        fun getContacts(context: Context): SqlCursor{
            val database = DataBaseCommunication.getDataBase(context)

            return database.contactQueries.GetAllContacts().execute()
        }

        fun getContactPhoneNumbers (context: Context, id: Long): SqlCursor{
            val database = DataBaseCommunication.getDataBase(context)

            return database.contactNumbersQueries.GetAllNumbers(id).execute()
        }

        fun getContactEmails(context: Context, id: Long): SqlCursor{
            val database = DataBaseCommunication.getDataBase(context)

            return database.contactEmailQueries.GetAllEmails(id).execute()
        }

        fun updateContact(context: Context, contact: ContactModel){
            val database = DataBaseCommunication.getDataBase(context)
                database.contactQueries.transaction {

                    afterCommit {}
                    afterRollback { Toast.makeText(context, R.string.insert_failed, Toast.LENGTH_SHORT).show() }

                    if (contact.dbOperationType == Utils.UPDATE)
                    database.contactQueries.UpdateUserName(contact.firstName,contact.lastName,contact.id!!)

                    if  (contact.phoneNumberModelList.isNotEmpty()){

                        for (number in contact.phoneNumberModelList){
                            when (number.dbOperationType){
                                Utils.INSERT -> database.contactNumbersQueries.InsertPhoneByID(contact.id,number.phoneNumber,number.phoneNumberType)
                                Utils.UPDATE -> database.contactNumbersQueries.UpdatePhoneNumber(number.phoneNumber,number.phoneNumberType,number.id!!)
                                Utils.DELETE -> database.contactNumbersQueries.DeletePhoneNumberById(number.id!!)
                            }
                        }
                    }
                    if (contact.emailModelList.isNotEmpty()){

                        for (email in contact.emailModelList){
                            when (email.dbOperationType){
                                Utils.INSERT -> database.contactEmailQueries.InsertEmailByID(contact.id,email.email,email.emailType)
                                Utils.UPDATE -> database.contactEmailQueries.UpdateEmail(email.email,email.emailType,email.id!!)
                                Utils.DELETE -> database.contactEmailQueries.DeleteEmailByID(email.id!!)
                            }
                        }
                    }
                }
        }

        fun deleteContactById(context: Context, id: Long){
            val database = DataBaseCommunication.getDataBase(context)
                database.contactQueries.transaction {
                    afterCommit {}
                    afterRollback { Toast.makeText(context, R.string.delete_failed, Toast.LENGTH_SHORT).show() }

                    database.contactEmailQueries.DeleteEmail(id)
                    database.contactNumbersQueries.DeletePhoneNumber(id)
                    database.contactQueries.DeleteContact(id)
                }
        }
}
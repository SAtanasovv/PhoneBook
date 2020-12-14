package com.satanasov.phonebook.kotlinPhoneBook.presenter

import android.content.Context
import com.satanasov.phonebook.kotlinPhoneBook.helpers.ContactsData
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel
import com.satanasov.phonebook.kotlinPhoneBook.model.PhoneNumberModel
import java.util.*
import kotlin.collections.ArrayList

class MainActivityPresenter(mainActivityView: MainActivityView): BasePresenter<MainActivityView>() {
    private var mDataBaseContactList: ArrayList<ContactModel>       = ArrayList()
    private var mPhoneStorageContactList: ArrayList<ContactModel>   = ArrayList()
    private var mMergedList: ArrayList<ContactModel>                = ArrayList()

    private var mContactsData: ContactsData

    init {
        attachView(mainActivityView)
        this.mContactsData     = ContactsData(mainActivityView as Context)
    }

    override fun subscribe() {
        super.subscribe()
        mPhoneStorageContactList = mContactsData.getContactsModelListFromPhoneStorage()
        mDataBaseContactList     = mContactsData.getContactModelListFromDataBase()
        mergeLists()
        view?.setContactListInRecyclerView(mMergedList)
    }

    private fun mergeLists() {
        for (dataBaseUser in mDataBaseContactList) {
            for (phoneBookUser in mPhoneStorageContactList) {
                for (dataBaseNumber in dataBaseUser.phoneNumberModelList) {
                    for (phoneBookNumber in phoneBookUser.phoneNumberModelList) {
                        if (dataBaseNumber.phoneNumber == phoneBookNumber.phoneNumber) {
                            val phoneNumberSet: MutableSet<PhoneNumberModel> = LinkedHashSet()
                            phoneNumberSet.addAll(dataBaseUser.phoneNumberModelList)
                            phoneNumberSet.addAll(phoneBookUser.phoneNumberModelList)
                            dataBaseUser.phoneNumberModelList = ArrayList(phoneNumberSet)
                        }
                    }
                }
            }
        }
        val userModelsSet: MutableSet<ContactModel> = LinkedHashSet()
        userModelsSet.addAll(mDataBaseContactList)
        userModelsSet.addAll(mPhoneStorageContactList)
        mMergedList = ArrayList(userModelsSet)
        mMergedList.sortBy { contactModel: ContactModel -> contactModel.firstName }
    }
}
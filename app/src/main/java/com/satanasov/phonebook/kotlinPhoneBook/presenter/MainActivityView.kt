package com.satanasov.phonebook.kotlinPhoneBook.presenter

import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel

interface MainActivityView {
    fun setContactListInRecyclerView(contactModel: ArrayList<ContactModel>)
}
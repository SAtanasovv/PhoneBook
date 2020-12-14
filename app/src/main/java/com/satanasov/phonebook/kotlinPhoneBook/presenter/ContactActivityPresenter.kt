package com.satanasov.phonebook.kotlinPhoneBook.presenter
import android.content.Context
import com.satanasov.phonebook.db.DataBaseQueries
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel

class ContactActivityPresenter(contactActivityView: ContactActivityView) : BasePresenter<ContactActivityView>() {
    private var mDataBaseQueries: DataBaseQueries = DataBaseQueries()
    private var mContactActivityView: ContactActivityView
    var mIsContactForEdit: Boolean = false

    init{
        attachView(contactActivityView)
        this.mContactActivityView = contactActivityView
    }

    override fun subscribe() {
        super.subscribe()
        if (mIsContactForEdit)
            view!!.setContactDetailsForEdit()
        else
            view!!.setContactDetailsForInsert()
    }

    fun saveContactToDB(contactModel: ContactModel){
        mDataBaseQueries.storeContact(mContactActivityView as Context,contactModel)
        view?.returnToMainActivity()
    }

    fun updateContactInDb(contactModel: ContactModel){
        mDataBaseQueries.updateContact(mContactActivityView as Context,contactModel)
        view?.returnToMainActivity()
    }
}
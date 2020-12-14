package com.satanasov.phonebook.presenter;
import android.content.Context;
import com.satanasov.phonebook.db.DataBaseQueries;
import com.satanasov.phonebook.model.ContactModel;

public class ContactActivityPresenter extends BasePresenter<ContactActivityView> {

    private DataBaseQueries     mDataBaseQueries  = new DataBaseQueries();
    private ContactActivityView mContactActivityView;
    public boolean              mIsContactForEdit = false;


   public ContactActivityPresenter(ContactActivityView contactActivityView){
       attachView(contactActivityView);
       this.mContactActivityView = contactActivityView;
   }

    @Override
    public void subscribe() {
        super.subscribe();
        if (mIsContactForEdit)
            view.setContactDetailsForEdit();
        else
            view.setContactDetailsForInsert();

    }

    public void saveContactToDB(ContactModel contactModel){

       //mDataBaseQueries.storeContact((Context)mContactActivityView,contactModel);
       view.returnToMainActivity();
    }

    public void updateContactInDB(ContactModel contactModel){
       //mDataBaseQueries.updateContact((Context)mContactActivityView,contactModel);
       view.returnToMainActivity();
    }
}

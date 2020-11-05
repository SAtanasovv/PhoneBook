package com.satanasov.phonebook.presenter;
import android.content.Context;
import com.satanasov.phonebook.helpers.ContactsData;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {
    private ArrayList<ContactModel> mDataBaseContactList     = new ArrayList<>();
    private ArrayList<ContactModel> mPhoneStorageContactList = new ArrayList<>();
    private ArrayList<ContactModel> mMergedList              = new ArrayList<>();

    private MainActivityView mMainActivityView;
    private ContactsData     mContactsData;

    public MainActivityPresenter(MainActivityView mainActivityView){
        attachView(mainActivityView);
        this.mMainActivityView  = mainActivityView;
        this.mContactsData      = new ContactsData((Context) mMainActivityView);
    }

    @Override
    public void subscribe() {
        super.subscribe();

        mPhoneStorageContactList = mContactsData.getContactsModelListFromPhoneStorage();
        mDataBaseContactList     = mContactsData.getContactModelListFromDataBase();
        mergeLists();
        view.setContactListInRecyclerView(mMergedList);
    }

    private void mergeLists(){
        for (ContactModel dataBaseUser : mDataBaseContactList){
            for(ContactModel phoneBookUser : mPhoneStorageContactList){
                for(PhoneNumberModel dataBaseNumber : dataBaseUser.getPhoneNumberModelList()) {
                    for (PhoneNumberModel phoneBookNumber : phoneBookUser.getPhoneNumberModelList()) {
                        if(dataBaseNumber.getPhoneNumber().equals(phoneBookNumber.getPhoneNumber())){

                            Set<PhoneNumberModel> phoneNumberSet = new LinkedHashSet<>();
                            phoneNumberSet.addAll(dataBaseUser.getPhoneNumberModelList());
                            phoneNumberSet.addAll(phoneBookUser.getPhoneNumberModelList());
                            dataBaseUser.setPhoneNumberModelList(new ArrayList<>(phoneNumberSet));
                        }
                    }
                }
            }
        }
        Set<ContactModel> userModelsSet = new LinkedHashSet<>();
        userModelsSet.addAll(mDataBaseContactList);
        userModelsSet.addAll(mPhoneStorageContactList);
        mMergedList = new ArrayList<>(userModelsSet);
        Collections.sort(mMergedList);
    }
}


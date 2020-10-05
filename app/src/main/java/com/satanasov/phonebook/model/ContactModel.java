package com.satanasov.phonebook.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ContactModel {
    private Long                                mId;
    private Bitmap                              mImageResource;
    private String                              mFirstName;
    private String                              mLastName;
    private ArrayList<ContactPhoneNumberModel>  mContactPhoneNumberModelsList;
    private ArrayList<ContactEmailModel>        mContactEmailModelsList;


    public ContactModel(String firstName, String lastName) {
        this.mFirstName = firstName;
        this.mLastName  = lastName;
    }

    public ContactModel(String firstName, String lastName, ArrayList<ContactPhoneNumberModel> contactPhoneNumberModelsList,ArrayList<ContactEmailModel> contactEmailModelsList  ) {
        this.mFirstName                    = firstName;
        this.mLastName                     = lastName;
        this.mContactEmailModelsList       = contactEmailModelsList;
        this.mContactPhoneNumberModelsList = contactPhoneNumberModelsList;
    }

    public ContactModel(String firstName, String lastName, Long Id, Bitmap imageResource, ArrayList<ContactPhoneNumberModel> contactPhoneNumberModelsList,ArrayList<ContactEmailModel> contactEmailModelsList  ) {
        this.mFirstName                    = firstName;
        this.mLastName                     = lastName;
        this.mId                           = Id;
        this.mImageResource                = imageResource;
        this.mContactEmailModelsList       = contactEmailModelsList;
        this.mContactPhoneNumberModelsList = contactPhoneNumberModelsList;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public Bitmap getImageId() {
        return mImageResource;
    }

    public void setImageId(Bitmap mImageId) {
        this.mImageResource = mImageId;
    }

    public ArrayList<ContactPhoneNumberModel> getContactPhoneNumberModels() {
        return mContactPhoneNumberModelsList;
    }

    public void setContactPhoneNumberModels(ArrayList<ContactPhoneNumberModel> mContactPhoneNumberModels) {
        this.mContactPhoneNumberModelsList = mContactPhoneNumberModels;
    }

    public ArrayList<ContactEmailModel> getContactEmailModels() {
        return mContactEmailModelsList;
    }

    public void setContactEmailModels(ArrayList<ContactEmailModel> mContactEmailModels) {
        this.mContactEmailModelsList = mContactEmailModels;
    }
}

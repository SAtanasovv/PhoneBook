package com.satanasov.phonebook.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class ContactModel implements Parcelable , Comparable<ContactModel>  {
    private Long       mId;
    private Bitmap     mImageResource;
    private String     mFirstName;
    private String     mLastName;

    private boolean    mExpanded;
    private boolean    mDataBaseContact;
    private int        mDBOperationType;
    private int        mContactPosition;

    private ArrayList  mPhoneNumberModelsList = new ArrayList();
    private ArrayList  mEmailModelsList       = new ArrayList() ;

    @Override
    public int hashCode() {
        return Objects.hash(mPhoneNumberModelsList);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        ContactModel ContactModel = (ContactModel) obj;
        return Objects.equals(mPhoneNumberModelsList,ContactModel.mPhoneNumberModelsList);
    }

    public ContactModel() {
        this.mExpanded        = false;
        this.mDataBaseContact = false;
    }

    public ContactModel(Long id, String firstName, String lastName) {
        this.mId              = id;
        this.mFirstName       = firstName;
        this.mLastName        = lastName;
        this.mExpanded        = false;
        this.mDataBaseContact = false;
    }

    public ContactModel(String firstName, String lastName, ArrayList<PhoneNumberModel> contactPhoneNumberModelsList, ArrayList<EmailModel> contactEmailModelsList, boolean dbContact ) {
        this.mFirstName              = firstName;
        this.mLastName               = lastName;
        this.mEmailModelsList        = contactEmailModelsList;
        this.mPhoneNumberModelsList  = contactPhoneNumberModelsList;
        this.mDataBaseContact        = dbContact;
        this.mExpanded               = false;
    }

    protected ContactModel(Parcel in) {
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }

        mImageResource         = in.readParcelable(Bitmap.class.getClassLoader());
        mFirstName             = in.readString();
        mLastName              = in.readString();
        mPhoneNumberModelsList = in.readArrayList(PhoneNumberModel.class.getClassLoader());
        mEmailModelsList       = in.readArrayList(EmailModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mId);
        }
        dest.writeParcelable(mImageResource, flags);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeList(mPhoneNumberModelsList);
        dest.writeList(mEmailModelsList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel in) {
            return new ContactModel(in);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };



    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.mExpanded = expanded;
    }

    public boolean isDataBaseContact() {
        return mDataBaseContact;
    }

    public void setDataBaseContact(boolean dataBaseContact) {
        this.mDataBaseContact = dataBaseContact;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public Bitmap getImageId() {
        return mImageResource;
    }

    public void setImageId(Bitmap imageId) {
        this.mImageResource = imageId;
    }

    public ArrayList<PhoneNumberModel> getPhoneNumberModelList() {
        return mPhoneNumberModelsList;
    }

    public void setPhoneNumberModelList(ArrayList<PhoneNumberModel> contactPhoneNumberModels) {
        this.mPhoneNumberModelsList = contactPhoneNumberModels;
    }

    public ArrayList<EmailModel> getEmailModelList() {
        return mEmailModelsList;
    }

    public void setEmailModelList(ArrayList<EmailModel> contactEmailModels) {
        this.mEmailModelsList = contactEmailModels;
    }

    public int getDBOperationType() {
        return mDBOperationType;
    }

    public void setDBOperationType(int dbOperationType) {
        this.mDBOperationType = dbOperationType;
    }

    public Integer getContactPosition() {
        return mContactPosition;
    }

    public void setContactPosition(int contactStored) {
        this.mContactPosition = contactStored;
    }


    @Override
    public int compareTo(ContactModel o) {
        return this.getFirstName().compareToIgnoreCase(o.getFirstName());
    }
}

package com.satanasov.phonebook.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class ContactModel implements Parcelable {
    private Long       mId;
    private Bitmap     mImageResource;
    private String     mFirstName;
    private String     mLastName;

    private boolean    mExpanded;

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
        this.mExpanded  = false;
    }

    public ContactModel(Long id, String firstName, String lastName) {
        this.mId        = id;
        this.mFirstName = firstName;
        this.mLastName  = lastName;
        this.mExpanded  = false;
    }

    public ContactModel(String firstName, String lastName, ArrayList<PhoneNumberModel> contactPhoneNumberModelsList, ArrayList<EmailModel> contactEmailModelsList  ) {
        this.mFirstName              = firstName;
        this.mLastName               = lastName;
        this.mEmailModelsList        = contactEmailModelsList;
        this.mPhoneNumberModelsList  = contactPhoneNumberModelsList;
        this.mExpanded  = false;

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

    public void setExpanded(boolean mExpanded) {
        this.mExpanded = mExpanded;
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

    public ArrayList<PhoneNumberModel> getPhoneNumberModelList() {
        return mPhoneNumberModelsList;
    }

    public void setPhoneNumberModelList(ArrayList<PhoneNumberModel> mContactPhoneNumberModels) {
        this.mPhoneNumberModelsList = mContactPhoneNumberModels;
    }

    public ArrayList<EmailModel> getEmailModelList() {
        return mEmailModelsList;
    }

    public void setEmailModelList(ArrayList<EmailModel> mContactEmailModels) {
        this.mEmailModelsList = mContactEmailModels;
    }
}

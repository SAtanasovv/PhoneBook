package com.satanasov.phonebook.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserModel implements Parcelable {

    private   ArrayList  mMainPhoneNumberList         = new ArrayList();
    private   ArrayList  mHomeNumberList              = new ArrayList();
    private   ArrayList  mWorkNumberList              = new ArrayList();
    private   ArrayList  mMobileNumberList            = new ArrayList();

    private   ArrayList mContactPhoneNumberList       = new ArrayList();
    private   ArrayList mContactEmailModelsList       = new ArrayList();

    private   Long       mID;
    private   String     mFirstName;
    private   String     mLastName;
    private   String     mEmail;
    private   Bitmap     mImageId;

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        mFirstName                    = in.readString();
        mLastName                     = in.readString();
        mMainPhoneNumberList          = in.readArrayList(String.class.getClassLoader());
        mHomeNumberList               = in.readArrayList(String.class.getClassLoader());
        mWorkNumberList               = in.readArrayList(String.class.getClassLoader());
        mMobileNumberList             = in.readArrayList(String.class.getClassLoader());
        mContactPhoneNumberList       = in.readArrayList(ContactPhoneNumberModel.class.getClassLoader());
        mContactEmailModelsList       = in.readArrayList(ContactEmailModel.class.getClassLoader());
        mEmail                        = in.readString();
        mImageId                      = in.readParcelable(getClass().getClassLoader());
    }

    public UserModel(Long id,String firstName, String lastName, ArrayList contactEmailModelsList,ArrayList contactPhoneNumberModelsList) {
        this.mID                           = id;
        this.mFirstName                    = firstName;
        this.mLastName                     = lastName;
        this.mContactEmailModelsList       = contactEmailModelsList;
        this.mContactPhoneNumberList       = contactPhoneNumberModelsList;
    }

    public UserModel(String firstName, String lastName, ArrayList<String> phoneNumber, String email, Bitmap imageId) {
        this.mFirstName            = firstName;
        this.mLastName             = lastName;
        this.mMainPhoneNumberList  = phoneNumber;
        this.mEmail                = email;
        this.mImageId              = imageId;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFirstName);
        parcel.writeString(mLastName);
        parcel.writeList(mMainPhoneNumberList);
        parcel.writeList(mHomeNumberList);
        parcel.writeList(mWorkNumberList);
        parcel.writeList(mMobileNumberList);
        parcel.writeList(mContactPhoneNumberList);
        //parcel.writeList(mContactEmailModelsList);
        parcel.writeString(mEmail);
        parcel.writeParcelable(mImageId,i);
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

    public ArrayList<String> getMainNumberList() {
        return mMainPhoneNumberList ;
    }

    public void setPhoneNumber(ArrayList<String> phoneNumber) {
        this.mMainPhoneNumberList  = phoneNumber;
    }

    public ArrayList<String> getHomeNumberList() {
        return mHomeNumberList;
    }

    public void setHomeNumberList(ArrayList mHomeNumberList) {
        this.mHomeNumberList = mHomeNumberList;
    }

    public ArrayList<String> getWorkNumberList() {
        return mWorkNumberList;
    }

    public void setWorkNumberList(ArrayList mWorkNumberList) {
        this.mWorkNumberList = mWorkNumberList;
    }

    public ArrayList<String> getMobileNumberList() {
        return mMobileNumberList;
    }

    public void setMobileNumberList(ArrayList mMobileNumberList) {
        this.mMobileNumberList = mMobileNumberList;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public Bitmap getImageId() {
        return mImageId;
    }

    public void setImageId(Bitmap mImageId) {
        this.mImageId = mImageId;
    }

    public ArrayList getMainPhoneNumberList() {
        return mMainPhoneNumberList;
    }

    public void setMainPhoneNumberList(ArrayList mMainPhoneNumberList) {
        this.mMainPhoneNumberList = mMainPhoneNumberList;
    }

    public ArrayList<ContactPhoneNumberModel> getContactPhoneNumberModelsList() {
        return mContactPhoneNumberList;
    }

    public void setContactPhoneNumberModelsList(ArrayList<ContactPhoneNumberModel> mContactPhoneNumberModelsList) {
        this.mContactPhoneNumberList = mContactPhoneNumberModelsList;
    }

    public ArrayList<ContactEmailModel> getContactEmailModelsList() {
        return mContactEmailModelsList;
    }

    public void setContactEmailModelsList(ArrayList<ContactEmailModel> mContactEmailModelsList) {
        this.mContactEmailModelsList = mContactEmailModelsList;
    }
}

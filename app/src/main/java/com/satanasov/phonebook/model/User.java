package com.satanasov.phonebook.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {

    private   ArrayList  mMainPhoneNumberList   = new ArrayList();
    private   ArrayList  mHomeNumberList        = new ArrayList();
    private   ArrayList  mWorkNumberList        = new ArrayList();
    private   ArrayList  mMobileNumberList      = new ArrayList();

    private   String     mFirstName;
    private   String     mLastName;
    private   String     mEmail;
    private   Bitmap     mImageId;

    public User() {
    }

    protected User(Parcel in) {
        mFirstName            = in.readString();
        mLastName             = in.readString();
        mMainPhoneNumberList  = in.readArrayList(String.class.getClassLoader());
        mHomeNumberList       = in.readArrayList(String.class.getClassLoader());
        mWorkNumberList       = in.readArrayList(String.class.getClassLoader());
        mMobileNumberList     = in.readArrayList(String.class.getClassLoader());
        mEmail                = in.readString();
        mImageId              = in.readParcelable(getClass().getClassLoader());
    }


    public User(String firstName, String lastName, ArrayList<String> phoneNumber, String email,Bitmap imageId) {
        this.mFirstName            = firstName;
        this.mLastName             = lastName;
        this.mMainPhoneNumberList  = phoneNumber;
        this.mEmail                = email;
        this.mImageId              = imageId;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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
}

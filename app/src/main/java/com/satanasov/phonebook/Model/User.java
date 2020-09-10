package com.satanasov.phonebook.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private             String      mFirstName;
    private             String      mLastName;
    private             String      mPhoneNumber;
    private             String      mEmail;
    private             int         mImageId;

    protected User(Parcel in) {
        mFirstName   = in.readString();
        mLastName    = in.readString();
        mPhoneNumber = in.readString();
        mEmail       = in.readString();
        mImageId     = in.readInt();
    }

    public User(String firstName, String lastName, String phoneNumber, String email,int imageId) {
        this.mFirstName   = firstName;
        this.mLastName    = lastName;
        this.mPhoneNumber = phoneNumber;
        this.mEmail       = email;
        this.mImageId     = imageId;
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
        parcel.writeString(mPhoneNumber);
        parcel.writeString(mEmail);
        parcel.writeInt(mImageId);
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

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }
    public int getImageId() {
        return mImageId;
    }
}

package com.satanasov.phonebook.model;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class PhoneNumberModel implements Serializable {
    private Long    mID;
    private Long    mContactID;
    private int     mRowId;
    private String  mPhoneNumber;
    private Long    mPhoneNumberType;
    private int     mDBOperationType;


    @Override
    public int hashCode() {
        return Objects.hash(mPhoneNumber);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PhoneNumberModel contactPhoneNumberModel = (PhoneNumberModel) obj;
        return Objects.equals(mPhoneNumber,contactPhoneNumberModel.mPhoneNumber);
    }

    public PhoneNumberModel(){

    }

    public PhoneNumberModel(String phoneNumber, Long phoneNumberType) {
        this.mPhoneNumber     = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
    }

    public PhoneNumberModel(String phoneNumber, Long phoneNumberType, int dBOperationType) {
        this.mPhoneNumber     = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
        this.mDBOperationType = dBOperationType;
    }

    public PhoneNumberModel(Long id, String phoneNumber, Long phoneNumberType) {
        this.mID              = id;
        this.mPhoneNumber     = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
    }

    public PhoneNumberModel(String phoneNumber, Long phoneNumberType, Long contactID, Long id) {
        this.mPhoneNumber     = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
        this.mContactID       = contactID;
        this.mID              = id;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public Long getPhoneNumberType() {
        return mPhoneNumberType;
    }

    public void setPhoneNumberType(Long phoneNumberType) {
        this.mPhoneNumberType = phoneNumberType;
    }

    public Long getContactID() {
        return mContactID;
    }

    public void setContactID(Long contactID) {
        this.mContactID = contactID;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long id) {
        this.mID = id;
    }

    public int getRowId() {
        return mRowId;
    }

    public void setRowId(int mRowId) {
        this.mRowId = mRowId;
    }

    public int getDBOperationType() {
        return mDBOperationType;
    }

    public void setDBOperationType(int dbOperationType) {
        this.mDBOperationType = dbOperationType;
    }
}

package com.satanasov.phonebook.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactPhoneNumberModel implements Serializable {
    private Long    mID;
    private Long    mContactID;
    private String  mPhoneNumber;
    private Long    mPhoneNumberType;


    public ContactPhoneNumberModel() {
    }

    public ContactPhoneNumberModel(String phoneNumber, Long phoneNumberType) {
        this.mPhoneNumber     = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
    }

    public ContactPhoneNumberModel(Long id,String phoneNumber, Long phoneNumberType) {
        this.mID              = id;
        this.mPhoneNumber     = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
    }

    public ContactPhoneNumberModel(String phoneNumber, Long phoneNumberType, Long contactID, Long id) {
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

    public void setContactID(Long mContactID) {
        this.mContactID = mContactID;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long mID) {
        this.mID = mID;
    }
}

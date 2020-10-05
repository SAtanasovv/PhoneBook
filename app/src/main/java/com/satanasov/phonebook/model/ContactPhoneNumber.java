package com.satanasov.phonebook.model;

import java.util.ArrayList;

public class ContactPhoneNumber {
    private String                 mPhoneNumber;
    private Long                   mPhoneNumberType;
    private Long                   mContactID;
    private ArrayList<ContactPhoneNumber> mContactPhoneNumbers;

    public ContactPhoneNumber() {
    }

    public ContactPhoneNumber(String phoneNumber, Long phoneNumberType) {
        this.mPhoneNumber = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
    }

    public ContactPhoneNumber(String phoneNumber, Long phoneNumberType, Long contactID) {
        this.mPhoneNumber = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
        this.mContactID = contactID;
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

    public ArrayList<ContactPhoneNumber> getPhoneNumbers() {
        return mContactPhoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<ContactPhoneNumber> mContactPhoneNumbers) {
        this.mContactPhoneNumbers = mContactPhoneNumbers;
    }

    public Long getContactID() {
        return mContactID;
    }

    public void setContactID(Long mContactID) {
        this.mContactID = mContactID;
    }
}

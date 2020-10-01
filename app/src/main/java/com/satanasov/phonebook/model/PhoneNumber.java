package com.satanasov.phonebook.model;

public class PhoneNumber {
    private String mPhoneNumber;
    private Long   mPhoneNumberType;

    public PhoneNumber(String phoneNumber, Long phoneNumberType) {
        this.mPhoneNumber = phoneNumber;
        this.mPhoneNumberType = phoneNumberType;
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
}

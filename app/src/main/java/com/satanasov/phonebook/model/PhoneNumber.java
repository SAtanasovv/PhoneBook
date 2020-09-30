package com.satanasov.phonebook.model;

public class PhoneNumber {
    private String phoneNumber;
    private Long    phoneNumberType;

    public PhoneNumber(String phoneNumber, Long phoneNumberType) {
        this.phoneNumber = phoneNumber;
        this.phoneNumberType = phoneNumberType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getPhoneNumberType() {
        return phoneNumberType;
    }

    public void setPhoneNumberType(Long phoneNumberType) {
        this.phoneNumberType = phoneNumberType;
    }
}

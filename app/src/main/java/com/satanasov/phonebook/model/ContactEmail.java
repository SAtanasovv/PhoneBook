package com.satanasov.phonebook.model;

public class ContactEmail {
    private String mEmail;
    private Long mContactID;

    public ContactEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public ContactEmail(String email, Long contactID) {
        this.mEmail = email;
        this.mContactID = contactID;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public Long getContactID() {
        return mContactID;
    }

    public void setContactID(Long mContactID) {
        this.mContactID = mContactID;
    }
}

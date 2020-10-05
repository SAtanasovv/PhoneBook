package com.satanasov.phonebook.model;

public class ContactEmailModel {
    private Long   mID;
    private Long   mContactID;
    private String mEmail;
    private Long   mEmailType;


    public ContactEmailModel(String mEmail) {
        this.mEmail = mEmail;
    }

    public ContactEmailModel(String email, Long emailType) {
        this.mEmail     = email;
        this.mEmailType = emailType;
    }
    public ContactEmailModel(String email, Long emailType, Long contactID, Long id) {
        this.mEmail     = email;
        this.mEmailType = emailType;
        this.mContactID = contactID;
        this.mID        = id;
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

    public Long getEmailType() {
        return mEmailType;
    }

    public void setEmailType(Long mEmailType) {
        this.mEmailType = mEmailType;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long mID) {
        this.mID = mID;
    }
}

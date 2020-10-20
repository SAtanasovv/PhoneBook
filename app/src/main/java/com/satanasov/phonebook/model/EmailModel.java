package com.satanasov.phonebook.model;

import java.io.Serializable;

public class EmailModel implements Serializable {
    private Long   mID;
    private Long   mContactID;
    private String mEmail;
    private Long   mEmailType;


    public EmailModel(String email, Long emailType) {
        this.mEmail     = email;
        this.mEmailType = emailType;
    }

    public EmailModel(Long id, String email, Long emailType) {
        this.mID        = id;
        this.mEmail     = email;
        this.mEmailType = emailType;
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

    public void setContactID(Long contactID) {
        this.mContactID = contactID;
    }

    public Long getEmailType() {
        return mEmailType;
    }

    public void setEmailType(Long emailType) {
        this.mEmailType = emailType;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long id) {
        this.mID = id;
    }
}

package com.satanasov.phonebook.model;

public class ContactName {
    private String mFirstName;
    private String mLastName;
    private Long   mId;

    public ContactName(String mFirstName, String mLastName) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }

    public ContactName(String mFirstName, String mLastName, Long mId) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mId = mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }
}

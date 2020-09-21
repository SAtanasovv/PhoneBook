package com.satanasov.phonebook.globalData;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.satanasov.phonebook.model.User;

import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

public class PhoneContacts {
private Context               mContext;
private static final String[] mPROJECTION =
        {
          ContactsContract.CommonDataKinds.Phone.NUMBER,
          ContactsContract.CommonDataKinds.Email.ADDRESS
        };

    public PhoneContacts(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<User> getContacts(){

    ArrayList<User> contactList       = new ArrayList<>();
    ArrayList<String> phoneNumberList = new ArrayList<>();

    Cursor cursor = mContext.getContentResolver().query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC ");

        if (cursor!=null && cursor.getCount()>0){
            while(cursor.moveToNext()){
                int    hasPhoneNumber     = 0;
                String contactId          = null;
                String contactName        = null;
                String contactPhoneNumber = null;
                String contactEmail       = null;
                User user = new User();

                contactId   = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                user.setFirstName(contactName);

                if(hasPhoneNumber>0) {
                    Cursor phoneNumberCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId}, null);
                    while (phoneNumberCursor != null && phoneNumberCursor.moveToNext()) {
                        int phoneNumberType = phoneNumberCursor.getInt(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        contactPhoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phoneNumberType==TYPE_MOBILE){
                            phoneNumberList.add("Mobile " + contactPhoneNumber);
                        }
                        if (phoneNumberType==TYPE_HOME){

                        }

                        phoneNumberList.add(contactPhoneNumber);
                        user.setPhoneNumber(phoneNumberList);

                        //phoneNumberCursor.close();
                    }
                }
                 Cursor phoneEmailCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, mPROJECTION, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                         new String[] {contactId}, null);
                if (phoneEmailCursor!=null && phoneEmailCursor.moveToNext()){
                    contactEmail = phoneEmailCursor.getString(phoneEmailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    user.setEmail(contactEmail);
                    phoneEmailCursor.close();
                }
                contactList.add(user);
            }
            cursor.close();
        }
        return contactList;
    }

}

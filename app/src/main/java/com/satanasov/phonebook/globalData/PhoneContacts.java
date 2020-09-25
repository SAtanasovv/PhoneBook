package com.satanasov.phonebook.globalData;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

import com.satanasov.phonebook.model.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MAIN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK;

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

    ArrayList<User> contactList = new ArrayList<>();

    Cursor cursor = mContext.getContentResolver().query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC ");

        if (cursor!=null && cursor.getCount()>0){
            while(cursor.moveToNext()){
                int    hasPhoneNumber = 0;
                String contactId;
                String contactName;
                String contactEmail;
                Bitmap contactPhoto;
                byte[] byteArray;
                User user = new User();

                contactId       = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contactName     = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                hasPhoneNumber  = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                user.setFirstName(contactName);

                if(hasPhoneNumber>0){
                    Cursor phoneNumberCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
               ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);
                    while (phoneNumberCursor != null && phoneNumberCursor.moveToNext()) {
                        int phoneNumberType = phoneNumberCursor.getInt(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String contactPhoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        switch(phoneNumberType){
                            case TYPE_HOME:
                                user.getHomeNumberList().add(contactPhoneNumber);
                                break;
                            case TYPE_MOBILE:
                                user.getMobileNumberList().add(contactPhoneNumber);
                                break;
                            case TYPE_WORK:
                                user.getWorkNumberList().add(contactPhoneNumber);
                                break;
                            case TYPE_MAIN:
                                user.getMainNumberList().add(contactPhoneNumber);
                                break;
                        }
                    }
                }
                 Cursor phoneEmailCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, mPROJECTION,
                  ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] {contactId}, null);

                if (phoneEmailCursor!=null && phoneEmailCursor.moveToNext()){
                    contactEmail = phoneEmailCursor.getString(phoneEmailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    user.setEmail(contactEmail);
                    phoneEmailCursor.close();
                }

                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(mContext.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(contactId)));

                if (inputStream != null){
                    contactPhoto = BitmapFactory.decodeStream(inputStream);
                    user.setImageId(contactPhoto);
                }

                if(user.getFirstName() != null)
                contactList.add(user);
            }
            cursor.close();
        }
        return contactList;
    }
}

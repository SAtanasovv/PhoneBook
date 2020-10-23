package com.satanasov.phonebook.helpers;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.db.DataBaseQueries;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.EmailModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.squareup.sqldelight.db.SqlCursor;
import java.io.InputStream;
import java.util.ArrayList;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MAIN;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_WORK;

public class ContactsData {
    public static int     CONTACT_ID          = 0;
    public static int     CONTACT_FIRST_NAME  = 1;
    public static int     CONTACT_LAST_NAME   = 2;

    public static int     CONTACT_NUMBER_ID   = 0;
    public static int     CONTACT_NUMBER      = 1;
    public static int     CONTACT_NUMBER_TYPE = 2;

    public static int     CONTACT_EMAIL_ID    = 0;
    public static int     CONTACT_EMAIL       = 1;
    public static int     CONTACT_EMAIL_TYPE  = 2;

    private Context         mContext;

    private DataBaseQueries          mDataBaseQueries         = new DataBaseQueries();
    private ArrayList<ContactModel>  mDataBaseContactList     = new ArrayList<>();
    private ArrayList<ContactModel>  mPhoneStorageContactList = new ArrayList<>();

    public ContactsData(Context context) {
        this.mContext = context;
    }

    public ArrayList<ContactModel> getContactModelListFromDataBase(){
        ContactModel     user;
        EmailModel       contactEmailModel;
        PhoneNumberModel phoneNumberModel;

        SqlCursor cursorContact = mDataBaseQueries.getContacts(mContext);

        while(cursorContact.next()) {
            ArrayList<EmailModel>       emailList       = new ArrayList<>();
            ArrayList<PhoneNumberModel> phoneNumberList = new ArrayList<>();

            user = new ContactModel(cursorContact.getLong(CONTACT_ID),cursorContact.getString(CONTACT_FIRST_NAME),cursorContact.getString(CONTACT_LAST_NAME));
            user.setDataBaseContact(true);

            SqlCursor cursorNumbers = mDataBaseQueries.getContactPhoneNumbers(mContext, user.getId());
            while (cursorNumbers.next()){

                phoneNumberModel = new PhoneNumberModel(cursorNumbers.getLong(CONTACT_NUMBER_ID),cursorNumbers.getString(CONTACT_NUMBER),cursorNumbers.getLong(CONTACT_NUMBER_TYPE));
                phoneNumberList.add(phoneNumberModel);
            }
            user.setPhoneNumberModelList(phoneNumberList);

            SqlCursor cursorEmails = mDataBaseQueries.getContactEmails(mContext,user.getId());
            while (cursorEmails.next()){

                contactEmailModel = new EmailModel(cursorEmails.getLong(CONTACT_EMAIL_ID),cursorEmails.getString(CONTACT_EMAIL),cursorEmails.getLong(CONTACT_EMAIL_TYPE));
                emailList.add(contactEmailModel);
            }
            user.setEmailModelList(emailList);

            mDataBaseContactList.add(user);
        }
        return mDataBaseContactList;
    }

    public ArrayList<ContactModel> getContactsModelListFromPhoneStorage(){
        String[] userNameProjection  = new String[]{

                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER
        };

        String[] phoneNumberProjection = new String[]{

                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String[] emailProjection = new String[]{

                ContactsContract.CommonDataKinds.Email.TYPE,
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        Cursor cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, userNameProjection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC ");

        if (cursor!=null && cursor.getCount()>0){

            while(cursor.moveToNext()){
                int    hasPhoneNumber = 0;
                String contactId;
                String contactName;
                Bitmap contactPhoto;

                ContactModel user = new ContactModel();

                contactId       = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                contactName     = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                hasPhoneNumber  = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                user.setId(Long.parseLong(contactId));
                user.setFirstName(contactName);
                user.setDataBaseContact(false);

                if(hasPhoneNumber>0){
                    Cursor phoneNumberCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, phoneNumberProjection,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contactId}, null);

                    while (phoneNumberCursor != null && phoneNumberCursor.moveToNext()) {

                        int phoneNumberType       = phoneNumberCursor.getInt(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String contactPhoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        switch(phoneNumberType){

                            case TYPE_HOME:
                                user.getPhoneNumberModelList().add(new PhoneNumberModel(contactPhoneNumber,Utils.HOME_PHONE_NUMBER));
                            break;

                            case TYPE_MOBILE:
                                user.getPhoneNumberModelList().add(new PhoneNumberModel(contactPhoneNumber,Utils.MOBILE_PHONE_NUMBER));
                            break;

                            case TYPE_WORK:
                                user.getPhoneNumberModelList().add(new PhoneNumberModel(contactPhoneNumber,Utils.WORK_PHONE_NUMBER));
                            break;

                            case TYPE_MAIN:
                                user.getPhoneNumberModelList().add(new PhoneNumberModel(contactPhoneNumber,Utils.MAIN_PHONE_NUMBER));
                            break;
                        }
                    }
                }
                Cursor phoneEmailCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, emailProjection,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] {contactId}, null);

                while(phoneEmailCursor!=null && phoneEmailCursor.moveToNext()) {

                    int emailType = phoneEmailCursor.getInt(phoneEmailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                    String email = phoneEmailCursor.getString(phoneEmailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                    switch (emailType){

                        case TYPE_HOME:
                            user.getEmailModelList().add(new EmailModel(email,Utils.HOME_EMAIL));
                        break;

                        case TYPE_WORK:
                            user.getEmailModelList().add(new EmailModel(email,Utils.WORK_EMAIL));
                        break;
                    }
                }
                phoneEmailCursor.close();

                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(mContext.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(contactId)));

                if (inputStream != null){
                    contactPhoto = BitmapFactory.decodeStream(inputStream);
                    user.setImageId(contactPhoto);
                }

                if(user.getFirstName() != null)
                    mPhoneStorageContactList.add(user);
            }
            cursor.close();
        }
        return mPhoneStorageContactList;
    }

    public String getSpinnerTypeText(Long typeID){
        String numberType = "";

        if(typeID.equals(Utils.HOME_PHONE_NUMBER))
            numberType = (mContext.getString(R.string.type_home));

        else if (typeID.equals(Utils.WORK_PHONE_NUMBER))
            numberType = (mContext.getString(R.string.type_work)) ;

        else if (typeID.equals(Utils.MOBILE_PHONE_NUMBER))
            numberType = (mContext.getString(R.string.type_mobile));

        else if (typeID.equals(Utils.MAIN_PHONE_NUMBER))
            numberType = (mContext.getString(R.string.type_main));

        return numberType;
        }


    public Long getSpinnerTypeID(String type){
        Long longID = 0L;

        if(type.equals(mContext.getString(R.string.type_home)))
            longID = Utils.HOME_PHONE_NUMBER;

        else if (type.equals(mContext.getString(R.string.type_work)))
            longID = (Utils.WORK_PHONE_NUMBER);

        else if (type.equals(mContext.getString(R.string.type_mobile)))
            longID = (Utils.MOBILE_PHONE_NUMBER);

        else if (type.equals(mContext.getString(R.string.type_main)))
            longID = (Utils.MAIN_PHONE_NUMBER);

        return longID;
        }

    }


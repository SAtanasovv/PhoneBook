package com.satanasov.phonebook.helpers;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.db.DataBaseQueries;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.EmailModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.squareup.sqldelight.db.SqlCursor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

    private Context          mContext;

    private DataBaseQueries  mDataBaseQueries = new DataBaseQueries();

    public ContactsData(Context context) {
        this.mContext = context;
    }

    public ArrayList<ContactModel> getContactModelListFromDataBase(){
        ContactModel     user;
        EmailModel       contactEmailModel;
        PhoneNumberModel phoneNumberModel;

        ArrayList<ContactModel>  mDataBaseContactList   = new ArrayList<>();

        SqlCursor cursorContact                         = mDataBaseQueries.getContacts(mContext);

        while(cursorContact.next()) {
            ArrayList<EmailModel>       emailList       = new ArrayList<>();
            ArrayList<PhoneNumberModel> phoneNumberList = new ArrayList<>();

            user = new ContactModel(cursorContact.getLong(CONTACT_ID),cursorContact.getString(CONTACT_FIRST_NAME),cursorContact.getString(CONTACT_LAST_NAME));
            user.setDataBaseContact(true);

            SqlCursor cursorNumbers = mDataBaseQueries.getContactPhoneNumbers(mContext, user.getId());
            while (cursorNumbers.next()){

                phoneNumberModel    = new PhoneNumberModel(cursorNumbers.getLong(CONTACT_NUMBER_ID),cursorNumbers.getString(CONTACT_NUMBER),cursorNumbers.getLong(CONTACT_NUMBER_TYPE));
                phoneNumberList.add(phoneNumberModel);
            }
            user.setPhoneNumberModelList(phoneNumberList);

            SqlCursor cursorEmails  = mDataBaseQueries.getContactEmails(mContext,user.getId());
            while (cursorEmails.next()){

                contactEmailModel   = new EmailModel(cursorEmails.getLong(CONTACT_EMAIL_ID),cursorEmails.getString(CONTACT_EMAIL),cursorEmails.getLong(CONTACT_EMAIL_TYPE));
                emailList.add(contactEmailModel);
            }
            user.setEmailModelList(emailList);

            mDataBaseContactList.add(user);
        }
        return mDataBaseContactList;
    }

    public ArrayList<ContactModel> getContactsModelListFromPhoneStorage(){
        ArrayList<ContactModel>  mPhoneStorageContactList;
        HashMap<Integer,ContactModel> contactById = new HashMap<>();

        BitmapUtils bitmapUtils                   = new BitmapUtils();

        String[] projection  = new String[]{
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DATA15,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Contactables.DATA,
                ContactsContract.CommonDataKinds.Contactables.TYPE
        };

        String selection = ContactsContract.Data.MIMETYPE + " in (?, ?, ?)";

        String[] selectionArgs = {
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
        };

        Uri uri          = ContactsContract.Data.CONTENT_URI;

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC " ;

        Cursor cursor    = mContext.getContentResolver().query(uri,projection,selection,selectionArgs,sortOrder);

        final int mimeTypeIndex = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        final int idIndex       = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        final int nameIndex     = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int dataIndex     = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DATA);
        final int typeIndex     = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.TYPE);
        final int photoIndex    = cursor.getColumnIndex(ContactsContract.Data.DATA15);

        while  (cursor.moveToNext()){

            long contactId         = cursor.getInt(idIndex);
            long contactType       = cursor.getInt(typeIndex);
            String contactName     = cursor.getString(nameIndex);
            String contactData     = cursor.getString(dataIndex);
            String contactMimeType = cursor.getString(mimeTypeIndex);
            byte[] contactPhoto    = cursor.getBlob(photoIndex);


            ContactModel contactModel = contactById.get( (int) contactId);

            if (contactModel == null){
                contactModel = new ContactModel(contactId,contactName);
        }
            if (contactMimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE))

                contactModel.getEmailModelList().add(new EmailModel(contactData,contactType));

            else if (contactMimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))

                contactModel.getPhoneNumberModelList().add(new PhoneNumberModel(contactData,contactType));

            else
                contactModel.setImageId(bitmapUtils.getImageFromBytes(contactPhoto));


            contactById.put((int) contactId,contactModel);
        }
        cursor.close();

        Collection<ContactModel> contactModels = contactById.values();
        mPhoneStorageContactList               = new ArrayList<>(contactModels);

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


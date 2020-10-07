package com.satanasov.phonebook.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.satanasov.phonebook.adapter.MainActivityRecycleAdapter;
import com.satanasov.phonebook.databinding.ActivityMainBinding;
import com.satanasov.phonebook.db.DataBaseQueries;
import com.satanasov.phonebook.globalData.PhoneContacts;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.EmailModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.globalData.Utils;
import com.squareup.sqldelight.db.SqlCursor;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends BaseActivity {
    private ArrayList<ContactModel>  mDataBaseList   = new ArrayList<>();
    private ArrayList<ContactModel>  mPhoneStorageList = new ArrayList<>();
    private ArrayList<ContactModel>  mMergedList = new ArrayList<>();
    private PhoneContacts         mPhoneContacts  = new PhoneContacts(this);
    private DataBaseQueries       dataBaseQueries = new DataBaseQueries();
    private RecyclerView          mRecyclerView;
    private RecyclerView.Adapter  mAdapter;
    private FloatingActionButton  mFloatingButton;

    public static final int       mPERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ActivityMainBinding           mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContactPermission(); // rename
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        //this.deleteDatabase("phoneBookContacts8.db");
        //dataBaseQueries.deleteContactById(this,5);
        //List list = dataBaseQueries.getAllContactsAsList(this);
        convertToUserModelList(); // rename
        mergeLists();
        init();
    }

    private void init(){
        mFloatingButton = findViewById(R.id.add_floating_button_main_activity_id);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContactsActivity(ChangeOptions.ADD_CONTACT);
            }
        });

        mRecyclerView  = mBinding.recyclerViewMainActivityId;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter       = new MainActivityRecycleAdapter(mMergedList,this);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar  = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void goToContactsActivity(ChangeOptions option){
        Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION,option);
        startActivity(intent);
    }

    public void requestContactPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_message);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[] {android.Manifest.permission.READ_CONTACTS}
                                , mPERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        mPERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else
           mPhoneStorageList.addAll(mPhoneContacts.getContacts());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == mPERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPhoneStorageList.addAll(mPhoneContacts.getContacts());
                Toast.makeText(this, R.string.permission_success, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, R.string.permission_unsuccessful, Toast.LENGTH_LONG).show();
        }
    }

    private void convertToUserModelList(){
        ContactModel user; // delete UserModel and start using ContactModel
        PhoneNumberModel phoneNumberModel;
        EmailModel contactEmailModel;

        SqlCursor cursorContact = dataBaseQueries.getContacts(this);

        while(cursorContact.next()) {
           ArrayList<PhoneNumberModel>  phoneNumberList = new ArrayList<>();
           ArrayList<EmailModel>        emailList       = new ArrayList<>();

           user = new ContactModel(cursorContact.getLong(Utils.CONTACT_ID),cursorContact.getString(Utils.CONTACT_FIRST_NAME),cursorContact.getString(Utils.CONTACT_LAST_NAME));
           SqlCursor cursorNumbers = dataBaseQueries.getContactPhoneNumbers(this, user.getId());

           while (cursorNumbers.next()){
               phoneNumberModel = new PhoneNumberModel(cursorNumbers.getLong(Utils.CONTACT_NUMBER_ID),cursorNumbers.getString(Utils.CONTACT_NUMBER),cursorNumbers.getLong(Utils.CONTACT_NUMBER_TYPE));
               phoneNumberList.add(phoneNumberModel);
           }

           user.setPhoneNumberModelList(phoneNumberList);

           SqlCursor cursorEmails = dataBaseQueries.getContactEmails(this,user.getId());

           while (cursorEmails.next()){
               contactEmailModel = new EmailModel(cursorEmails.getLong(Utils.CONTACT_EMAIL_ID),cursorEmails.getString(Utils.CONTACT_EMAIL),cursorEmails.getLong(Utils.CONTACT_EMAIL_TYPE));
               emailList.add(contactEmailModel);
           }

           user.setEmailModelList(emailList);
           mDataBaseList.add(user);
       }
    }

    private void mergeLists(){
        for (ContactModel dataBaseUser : mDataBaseList){
            for(ContactModel phoneBookUser : mPhoneStorageList){
                for(PhoneNumberModel dataBaseNumber : dataBaseUser.getPhoneNumberModelList()) {
                    for (PhoneNumberModel phoneBookNumber : phoneBookUser.getPhoneNumberModelList()) {
                        if(dataBaseNumber.getPhoneNumber().equals(phoneBookNumber.getPhoneNumber())){

                            Set<PhoneNumberModel> phoneNumberSet = new LinkedHashSet<>();
                            phoneNumberSet.addAll(dataBaseUser.getPhoneNumberModelList());
                            phoneNumberSet.addAll(phoneBookUser.getPhoneNumberModelList());
                            dataBaseUser.setPhoneNumberModelList(new ArrayList<>(phoneNumberSet));
                        }
                    }
                }
            }
        }
        Set<ContactModel> userModelsSet = new LinkedHashSet<>();
        userModelsSet.addAll(mDataBaseList);
        userModelsSet.addAll(mPhoneStorageList);
        mMergedList = new ArrayList<>(userModelsSet);
    }
}
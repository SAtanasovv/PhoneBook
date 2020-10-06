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
import com.satanasov.phonebook.model.ContactEmailModel;
import com.satanasov.phonebook.model.ContactPhoneNumberModel;
import com.satanasov.phonebook.model.UserModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.globalData.Utils;
import com.squareup.sqldelight.db.SqlCursor;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ArrayList<UserModel>  mDataBaseList   = new ArrayList<>();
    private ArrayList<UserModel>  mDummyUsersList = new ArrayList<>();
    private ArrayList<UserModel>  mMergedList = new ArrayList<>();
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
        requestContactPermission();
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        //this.deleteDatabase("phoneBookContacts8.db");
        //dataBaseQueries.deleteContactById(this,5);
        //List list = dataBaseQueries.getAllContactsAsList(this);
        convertToUserModelList();
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
           mDummyUsersList.addAll(mPhoneContacts.getContacts());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == mPERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mDummyUsersList.addAll(mPhoneContacts.getContacts());
                Toast.makeText(this, R.string.permission_success, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, R.string.permission_unsuccessful, Toast.LENGTH_LONG).show();
        }
    }

    private void convertToUserModelList(){
        UserModel user = new UserModel();
        ContactPhoneNumberModel phoneNumberModel;
        ContactEmailModel contactEmailModel;
        SqlCursor cursor = dataBaseQueries.getAllContacts(this);
        ArrayList<ContactPhoneNumberModel> phoneNumberList = new ArrayList<>();
        ArrayList<ContactEmailModel> emailList = new ArrayList<>();
        Long userID = 0L;
        Long emailID = 0L;
       while(cursor.next()) {
           if (cursor.getLong(1) != userID) {
               if(userID!=0L){
               mDataBaseList.add(user);}
               userID = cursor.getLong(1);
               phoneNumberList = new ArrayList<>();
               emailList = new ArrayList<>();
           }
               phoneNumberModel = new ContactPhoneNumberModel(cursor.getLong(7),cursor.getString(8),cursor.getLong(9));
               phoneNumberList.add(phoneNumberModel);
               contactEmailModel = new ContactEmailModel(cursor.getLong(4),cursor.getString(5),cursor.getLong(6));

               if(emailID!=cursor.getLong(4)){
                   emailList.add(contactEmailModel);
                   emailID = cursor.getLong(4);
           }
               user = new UserModel(cursor.getLong(1), cursor.getString(2), cursor.getString(3),emailList,phoneNumberList);
       }
       mDataBaseList.add(user);
    }

    private void mergeLists(){
        mMergedList.addAll(mDummyUsersList);
        mMergedList.removeAll(mDataBaseList);
        mMergedList.addAll(mDataBaseList);

    }
    private void transferContacts(){

    }
}
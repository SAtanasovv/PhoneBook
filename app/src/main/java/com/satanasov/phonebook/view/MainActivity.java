package com.satanasov.phonebook.view;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
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
import com.satanasov.phonebook.Helpers.ContactsData;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.globalData.Utils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends BaseActivity {
    private ArrayList<ContactModel>  mDataBaseContactList     = new ArrayList<>();
    private ArrayList<ContactModel>  mPhoneStorageContactList = new ArrayList<>();
    private ArrayList<ContactModel>  mMergedList              = new ArrayList<>();

    private ContactsData          mContactsData               = new ContactsData(this);

    private RecyclerView          mRecyclerView;
    private RecyclerView.Adapter  mAdapter;
    private FloatingActionButton  mFloatingButton;
    private ProgressBar           mLoadingBar;

    public static final int       mPERMISSIONS_REQUEST_READ_CONTACTS = 1;

    ActivityMainBinding           mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //deleteDatabase("phoneBookContacts8.db");
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        init();
        getPermissionToReadContactsFromInternalStorage();
        mDataBaseContactList = mContactsData.getContactModelListFromDataBase();
        mergeLists();

    }

    private void init(){
        mLoadingBar     = findViewById(R.id.loading_bar);
        mFloatingButton = findViewById(R.id.add_floating_button_main_activity_id);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContactsActivity(ChangeOptions.ADD_CONTACT);
            }
        });

        Toolbar toolbar  = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initAdapter(){
        mAdapter      = new MainActivityRecycleAdapter(mMergedList,this);
        mRecyclerView = mBinding.recyclerViewMainActivityId;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void goToContactsActivity(ChangeOptions option){
        Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION,option);
        startActivity(intent);
    }

    public void getPermissionToReadContactsFromInternalStorage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_message);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[] {android.Manifest.permission.READ_CONTACTS}, mPERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                });
                builder.show();
            }
            else
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, mPERMISSIONS_REQUEST_READ_CONTACTS);

        }
        else
            mPhoneStorageContactList.addAll(mContactsData.getContactsModelListFromPhoneStorage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mLoadingBar.setVisibility(View.VISIBLE);
        if (requestCode == mPERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPhoneStorageContactList.addAll(mContactsData.getContactsModelListFromPhoneStorage());
            }
            else
                Toast.makeText(this, R.string.permission_unsuccessful, Toast.LENGTH_LONG).show();
        }
    }

    private void mergeLists(){
        for (ContactModel dataBaseUser : mDataBaseContactList){
            for(ContactModel phoneBookUser : mPhoneStorageContactList){
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
        userModelsSet.addAll(mDataBaseContactList);
        userModelsSet.addAll(mPhoneStorageContactList);
        mMergedList = new ArrayList<>(userModelsSet);
        initAdapter();
    }
}
package com.satanasov.phonebook.view;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
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
import com.satanasov.phonebook.helpers.ContactsData;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.globalData.Utils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends BaseActivity {
    private ArrayList<ContactModel>  mDataBaseContactList     = new ArrayList<>();
    private ArrayList<ContactModel>  mPhoneStorageContactList = new ArrayList<>();
    private ArrayList<ContactModel>  mMergedList              = new ArrayList<>();

    private ContactsData             mContactsData            = new ContactsData(this);

    private RecyclerView             mRecyclerView;
    private FloatingActionButton     mFloatingButton;

    private MainActivityRecycleAdapter  mAdapter;

    public static final int       mPERMISSIONS_REQUEST_READ_CONTACTS = 1;

    ActivityMainBinding           mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        init();
        mDataBaseContactList = mContactsData.getContactModelListFromDataBase();
        getPermissionToReadContactsFromInternalStorage();
        mergeLists(mDataBaseContactList);
        initAdapter();
    }

    private void init(){
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
        mAdapter      = new MainActivityRecycleAdapter(mMergedList,(Context) this);
        mRecyclerView = mBinding.recyclerViewMainActivityId;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void goToContactsActivity(ChangeOptions option){
        Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION,option);
        startActivityForResult(intent,Utils.GO_TO_CONTACT_ACTIVITY_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.GO_TO_CONTACT_ACTIVITY_ADD){
            if (resultCode == Activity.RESULT_OK){
                mergeLists(mContactsData.getContactModelListFromDataBase());

                mAdapter.updateAdapterData(mMergedList);
                mAdapter.notifyDataSetChanged();
            }
        }

        if (requestCode == Utils.GO_TO_CONTACT_ACTIVITY_EDIT){
            if (resultCode == Activity.RESULT_OK){
                mergeLists(mContactsData.getContactModelListFromDataBase());

                mAdapter.updateAdapterData(mMergedList);
                mAdapter.notifyDataSetChanged();
            }
        }
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
        if (requestCode == mPERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mPhoneStorageContactList.addAll(mContactsData.getContactsModelListFromPhoneStorage());
                Collections.sort(mPhoneStorageContactList);
                mAdapter.updateAdapterData(mPhoneStorageContactList);
                mAdapter.notifyDataSetChanged();
            }
            else
                Toast.makeText(this, R.string.permission_unsuccessful, Toast.LENGTH_LONG).show();
        }
    }

    private void mergeLists(@NotNull ArrayList<ContactModel> dataBaseContactList){
        for (ContactModel dataBaseUser : dataBaseContactList){
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
        userModelsSet.addAll(dataBaseContactList);
        userModelsSet.addAll(mPhoneStorageContactList);
        mMergedList = new ArrayList<>(userModelsSet);
        Collections.sort(mMergedList);
    }
}
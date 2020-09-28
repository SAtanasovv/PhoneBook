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
import com.satanasov.phonebook.db.DataBaseHandler;
import com.satanasov.phonebook.globalData.PhoneContacts;
import com.satanasov.phonebook.model.User;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.globalData.Utils;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends BaseActivity {
    private ArrayList<User>       mDummyUsersList = new ArrayList<>();
    private PhoneContacts         mPhoneContacts  = new PhoneContacts(this);

    private RecyclerView          mRecyclerView;
    private RecyclerView.Adapter  mAdapter;

    private FloatingActionButton  mFloatingButton;

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ActivityMainBinding           binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContactPermission();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        //dummyUsers();
        DataBaseHandler dataBaseHandler = DataBaseHandler.getInstance(this);
        dataBaseHandler.insertContact("slav","atanasov");
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

        mRecyclerView  = binding.recyclerViewMainActivityId;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter       = new MainActivityRecycleAdapter(mDummyUsersList,this);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar  = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

    }

    private void goToContactsActivity(ChangeOptions option){
        Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION,option);
        startActivity(intent);
    }

//    private void dummyUsers(){
//        User user  = new User("Ivan","Petrov","1","a",R.drawable.png1);
//        User user1 = new User("Slav","Atanasov","2","b",R.drawable.png2);
//        User user2 = new User("Dragan","Lazarov","3","c",R.drawable.png2);
//        User user3 = new User("Petkan","Petkov","4","d",R.drawable.png1);
//        User user4 = new User("Ala","Bala","5","e",R.drawable.png1);
//
//        mDummyUsersList.add(user);
//        mDummyUsersList.add(user1);
//        mDummyUsersList.add(user2);
//        mDummyUsersList.add(user3);
//        mDummyUsersList.add(user4);
//    }
    public Comparator<User> compareByName = new Comparator<User>() {
        @Override
        public int compare(User user, User t1) {
            return user.getFirstName().compareToIgnoreCase(t1.getFirstName());
        }
    };

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
                                , PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else
           mDummyUsersList.addAll(mPhoneContacts.getContacts());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mDummyUsersList.addAll(mPhoneContacts.getContacts());
                Toast.makeText(this, R.string.permission_success, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, R.string.permission_unsuccessful, Toast.LENGTH_LONG).show();
        }
    }
}
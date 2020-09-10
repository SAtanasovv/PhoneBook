package com.satanasov.phonebook.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.satanasov.phonebook.Adapter.MainActivityRecycleAdapter;
import com.satanasov.phonebook.Model.User;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.GlobalData.Utils.ChangeOptions;
import com.satanasov.phonebook.GlobalData.Utils.IntentKeys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends BaseActivity {
    private ArrayList<User>             mDummyUsersList = new ArrayList<>();

    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;

    private FloatingActionButton        mFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dummyUsers();
        init();
    }

    private void init(){
        mFloatingButton  = findViewById(R.id.add_floating_button_main_activity_id);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContactsActivity(ChangeOptions.ADD_CONTACT);
            }
        });

        mRecyclerView   = findViewById(R.id.recycler_view_main_activity_id);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter        = new MainActivityRecycleAdapter(mDummyUsersList,this);
        Collections.sort(mDummyUsersList, compareByName);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void goToContactsActivity(ChangeOptions option){
        Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
        intent.putExtra(IntentKeys.OPTION.name(),option);
        startActivity(intent);
    }

    private void dummyUsers(){
        User user  = new User("Ivan","Petrov","1","a",R.drawable.png1);
        User user1 = new User("Slav","Atanasov","2","b",R.drawable.png2);
        User user2 = new User("Dragan","Lazarov","3","c",R.drawable.png2);
        User user3 = new User("Petkan","Petkov","4","d",R.drawable.png1);
        User user4 = new User("Ala","Bala","5","e",R.drawable.png1);

        mDummyUsersList.add(user);
        mDummyUsersList.add(user1);
        mDummyUsersList.add(user2);
        mDummyUsersList.add(user3);
        mDummyUsersList.add(user4);
    }
    public Comparator<User> compareByName = new Comparator<User>() {
        @Override
        public int compare(User user, User t1) {
            return user.getFirstName().compareToIgnoreCase(t1.getFirstName());
        }
    };
}
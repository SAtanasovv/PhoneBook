package com.satanasov.phonebook.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.satanasov.phonebook.Model.User;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.Utils.ChangeOptions;
import com.satanasov.phonebook.View.BaseActivity;

public class MainActivity extends BaseActivity {

    private                 Button      addContactBtn;
    private                 Button      editContactBtn;
    private                 Button      viewContactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addContactBtn = findViewById(R.id.add_button_main_activity_id);
        editContactBtn = findViewById(R.id.edit_button_main_activity_id);
        viewContactBtn = findViewById(R.id.view_button_main_activity_id);

    }

    @Override
    protected void onResume() {
        super.onResume();


        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
               intent.putExtra("changeOption", ChangeOptions.ADD_CONTACT.getOption());
               startActivity(intent);


            }
        });

        editContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
                intent.putExtra("changeOption", ChangeOptions.EDIT_CONTACT.getOption());
                intent.putExtra("contact",new User("Slav","Atanasov","0896847777","slav.1995"));
                startActivity(intent);

            }
        });

        viewContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
                intent.putExtra("changeOption", ChangeOptions.VIEW_CONTACT.getOption());
                intent.putExtra("contact",new User("Slav","Atanasov","0896847777","slav.1995"));
                startActivity(intent);


            }
        });

    }


}
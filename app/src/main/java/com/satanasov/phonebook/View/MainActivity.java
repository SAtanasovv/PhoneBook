package com.satanasov.phonebook.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.satanasov.phonebook.Model.User;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.GlobalData.Utils.ChangeOptions;
import com.satanasov.phonebook.GlobalData.Utils.IntentKeys;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button  mAddContactBtn;
    private Button  mEditContactBtn;
    private Button  mViewContactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_button_main_activity_id:
                goToContactsActivity(ChangeOptions.ADD_CONTACT);
                break;
            case R.id.edit_button_main_activity_id:
                goToContactsActivity(ChangeOptions.EDIT_CONTACT);
                break;
            case R.id.view_button_main_activity_id:
                goToContactsActivity(ChangeOptions.VIEW_CONTACT);
        }
    }

    private void init(){
        mAddContactBtn   = findViewById(R.id.add_button_main_activity_id);
        mEditContactBtn  = findViewById(R.id.edit_button_main_activity_id);
        mViewContactBtn  = findViewById(R.id.view_button_main_activity_id);

        mAddContactBtn.setOnClickListener(this);
        mEditContactBtn.setOnClickListener(this);
        mViewContactBtn.setOnClickListener(this);
    }

    private void goToContactsActivity(ChangeOptions option){
        Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
        intent.putExtra(IntentKeys.OPTION.name(),option);
        intent.putExtra(IntentKeys.CONTACT.name(),new User("Slav","Atanasov","0896847777","slav.1995"));
        startActivity(intent);
    }

}
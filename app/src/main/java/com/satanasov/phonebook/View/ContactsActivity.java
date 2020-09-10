package com.satanasov.phonebook.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.satanasov.phonebook.GlobalData.Utils;
import com.satanasov.phonebook.GlobalData.Utils.ChangeOptions;
import com.satanasov.phonebook.Model.User;
import com.satanasov.phonebook.R;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    private TextView        mFirstNameTextView;
    private TextView        mLastNameTextView;
    private TextView        mPhoneNumberTextView;
    private TextView        mEmailTextView;

    private Button          mSaveBtn;
    private Button          mCancelBtn;

    private User            mUser;
    private ChangeOptions   mOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_button_contacts_id:
                startActivity(new Intent(ContactsActivity.this,MainActivity.class));
                break;
            case R.id.save_button_contacts_id:
                break;
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            checkFields();
        }
    };

    private void init(){
        mFirstNameTextView   = findViewById(R.id.first_name_edit_text_contacts_id);
        mLastNameTextView    = findViewById(R.id.last_name_edit_text_contacts_id);
        mPhoneNumberTextView = findViewById(R.id.phone_number_edit_text_contacts_id);
        mEmailTextView       = findViewById(R.id.email_edit_text_contacts_id);

        mSaveBtn             = findViewById(R.id.save_button_contacts_id);
        mCancelBtn           = findViewById(R.id.cancel_button_contacts_id);

        mSaveBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        mFirstNameTextView.addTextChangedListener(watcher);
        mLastNameTextView.addTextChangedListener(watcher);
        mPhoneNumberTextView.addTextChangedListener(watcher);
        mEmailTextView.addTextChangedListener(watcher);

        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            mOption = (ChangeOptions) bundle.getSerializable(Utils.IntentKeys.OPTION.name());
            mUser   = bundle.getParcelable(Utils.IntentKeys.CONTACT.name());
            changeOptions(mOption);
        }
    }

    private void changeOptions(ChangeOptions option){
        switch(option){
            case ADD_CONTACT:
                mSaveBtn.setEnabled(false);
            break;
            case EDIT_CONTACT:
                setUser();
                mSaveBtn.setEnabled(false);
            break;
            case VIEW_CONTACT:
                setUser();
                disableFields();
                mSaveBtn.setVisibility(View.GONE);
            break;
        }
    }

    private void checkFields(){
        if(         mFirstNameTextView.getText().toString().isEmpty()                           ||
                    mLastNameTextView.getText().toString().isEmpty()                            ||
                    mEmailTextView.getText().toString().isEmpty()                               ||
                    mPhoneNumberTextView.getText().toString().isEmpty()){
            mSaveBtn.setEnabled(false);
        }
        else if(    mUser.getFirstName().equals(mFirstNameTextView.getText().toString())        &&
                    mUser.getLastName().equals(mLastNameTextView.getText().toString())          &&
                    mUser.getEmail().equals(mEmailTextView.getText().toString())                &&
                    mUser.getPhoneNumber().equals(mPhoneNumberTextView.getText().toString())    &&
                    mOption.equals(ChangeOptions.EDIT_CONTACT)){
            mSaveBtn.setEnabled(false);
        }
        else
            mSaveBtn.setEnabled(true);
    }

    private void disableFields(){
        mFirstNameTextView.setEnabled(false);
        mLastNameTextView.setEnabled(false);
        mPhoneNumberTextView.setEnabled(false);
        mEmailTextView.setEnabled(false);
    }

    private void setUser(){
        mFirstNameTextView.setText(mUser.getFirstName());
        mLastNameTextView.setText(mUser.getLastName());
        mPhoneNumberTextView.setText(mUser.getPhoneNumber());
        mEmailTextView.setText(mUser.getEmail());
    }
}
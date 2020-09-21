package com.satanasov.phonebook.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.satanasov.phonebook.databinding.ActivityContactsBinding;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.model.User;
import com.satanasov.phonebook.R;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    ActivityContactsBinding mContactsBinding;

    private TextInputEditText  mFirstNameEditText;
    private TextInputEditText  mLastNameEditText;
    private TextInputEditText  mPhoneNumberEditText;
    private TextInputEditText  mEmailEditText;

    private TextInputLayout    mFirstNameTextInputLayout;
    private TextInputLayout    mLastNameTextInputLayout;
    private TextInputLayout    mPhoneNumberTextInputLayout;
    private TextInputLayout    mEmailTextInputLayout;

    private Button             mSaveBtn;
    private Button             mCancelBtn;

    private User               mUser;
    private ChangeOptions      mOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsBinding = DataBindingUtil.setContentView(this,R.layout.activity_contacts);
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
//            if(mOption.equals(ChangeOptions.EDIT_CONTACT))
//                mSaveBtn.setEnabled(true);
            checkIfFieldsAreEmpty();
            checkErrorsInFields(editable);
        }
    };

    private void init(){
        mFirstNameEditText          = findViewById(R.id.first_name_edit_text_contacts_id);
        mLastNameEditText           = findViewById(R.id.last_name_edit_text_contacts_id);
        mPhoneNumberEditText        = findViewById(R.id.phone_number_edit_text_contacts_id);
        mEmailEditText              = findViewById(R.id.email_edit_text_contacts_id);

        mFirstNameTextInputLayout   = findViewById(R.id.input_layout_first_name_contacts_id);
        mLastNameTextInputLayout    = findViewById(R.id.input_layout_last_name_contacts_id);
        mPhoneNumberTextInputLayout = findViewById(R.id.input_layout_phone_number_contacts_id);
        mEmailTextInputLayout       = findViewById(R.id.input_layout_email_contacts_id);

        mSaveBtn                    = findViewById(R.id.save_button_contacts_id);
        mCancelBtn                  = findViewById(R.id.cancel_button_contacts_id);

        Toolbar toolbar             = findViewById(R.id.contacts_toolbar);
        setSupportActionBar(toolbar);

        mSaveBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        mFirstNameEditText.addTextChangedListener(watcher);
        mLastNameEditText.addTextChangedListener(watcher);
        mPhoneNumberEditText.addTextChangedListener(watcher);
        mEmailEditText.addTextChangedListener(watcher);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            mOption = (ChangeOptions) bundle.getSerializable(Utils.INTENT_EXTRA_OPTION);
            mUser   = bundle.getParcelable(Utils.INTENT_USER_DETAILS);
            changeOptions(mOption);
        }
    }

    private void changeOptions(ChangeOptions option){
        switch(option){
            case ADD_CONTACT:
                mSaveBtn.setEnabled(false);
                getSupportActionBar().setTitle(R.string.add_contact);
            break;
            case EDIT_CONTACT:
                mContactsBinding.setUser(mUser);
                getSupportActionBar().setTitle(R.string.edit_contact);
            break;
            case VIEW_CONTACT:
                mContactsBinding.setUser(mUser);
                disableFields();
                getSupportActionBar().setTitle(R.string.view_contact);
                mSaveBtn.setVisibility(View.GONE);
            break;
        }
    }

    private void checkIfFieldsAreEmpty(){
        if( mFirstNameEditText.getText().toString().isEmpty()    ||
            mLastNameEditText.getText().toString().isEmpty()     ||
            mEmailEditText.getText().toString().isEmpty()        ||
            mPhoneNumberEditText.getText().toString().isEmpty())
        {
            mSaveBtn.setEnabled(false);
        }
        else
            mSaveBtn.setEnabled(true);
    }

    private void checkErrorsInFields(Editable editable){
        if( editable.length()>mFirstNameTextInputLayout.getCounterMaxLength()   &&
            mFirstNameEditText.hasFocus() )
        {
            mFirstNameTextInputLayout.setError( getString(R.string.input_text_error_message)   +
            mFirstNameTextInputLayout.getCounterMaxLength());
        }
        else
            mFirstNameTextInputLayout.setError(null);
        if( editable.length()>mLastNameTextInputLayout.getCounterMaxLength()    &&
            mLastNameEditText.hasFocus())
        {
            mLastNameTextInputLayout.setError( getString(R.string.input_text_error_message)    +
            mLastNameTextInputLayout.getCounterMaxLength());
        }
        else
            mLastNameTextInputLayout.setError(null);
        if( editable.length()>mPhoneNumberTextInputLayout.getCounterMaxLength()  &&
            mPhoneNumberEditText.hasFocus())
        {
            mPhoneNumberTextInputLayout.setError( getString(R.string.input_text_error_message)  +
            mPhoneNumberTextInputLayout.getCounterMaxLength());
        }
        else
            mPhoneNumberTextInputLayout.setError(null);
        if( editable.length()>mEmailTextInputLayout.getCounterMaxLength()        &&
            mEmailTextInputLayout.hasFocus())
        {
            mEmailTextInputLayout.setError( getString(R.string.input_text_error_message)        +
            mEmailTextInputLayout.getCounterMaxLength());
        }
        else
            mEmailTextInputLayout.setError(null);
    }

    private void disableFields(){
        mFirstNameEditText.setEnabled(false);
        mLastNameEditText.setEnabled(false);
        mPhoneNumberEditText.setEnabled(false);
        mEmailEditText.setEnabled(false);
    }
}
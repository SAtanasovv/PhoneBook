package com.satanasov.phonebook.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.satanasov.phonebook.databinding.ActivityContactsBinding;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.model.User;
import com.satanasov.phonebook.R;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    ActivityContactsBinding mContactsBinding;

    private TextInputEditText             mFirstNameEditText;
    private TextInputEditText             mLastNameEditText;
    private MaterialAutoCompleteTextView  mMobileNumberAutoCompleteTextView;
    private MaterialAutoCompleteTextView  mWorkNumberAutoCompleteTextView;
    private MaterialAutoCompleteTextView  mHomeNumberAutoCompleteTextView;
    private MaterialAutoCompleteTextView  mMainNumberAutoCompleteTextView;
    private TextInputEditText             mEmailEditText;

    private TextInputLayout    mFirstNameTextInputLayout;
    private TextInputLayout    mLastNameTextInputLayout;
    private TextInputLayout    mMobileNumberTextInputLayout;
    private TextInputLayout    mHomeNumberTextInputLayout;
    private TextInputLayout    mMainNumberTextInputLayout;
    private TextInputLayout    mWorkNumberTextInputLayout;
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
            checkIfFieldsAreEmpty();
            checkErrorsInFields(editable);
        }
    };

    private void init(){
        mFirstNameEditText                 = findViewById(R.id.first_name_edit_text_contacts_id);
        mLastNameEditText                  = findViewById(R.id.last_name_edit_text_contacts_id);
        mEmailEditText                     = findViewById(R.id.email_edit_text_contacts_id);

        mMobileNumberAutoCompleteTextView  = findViewById(R.id.mobile_number_edit_text_contacts_id);
        mWorkNumberAutoCompleteTextView    = findViewById(R.id.work_number_edit_text_contacts_id);
        mHomeNumberAutoCompleteTextView    = findViewById(R.id.home_number_edit_text_contacts_id);
        mMainNumberAutoCompleteTextView    = findViewById(R.id.main_number_edit_text_contacts_id);

        mFirstNameTextInputLayout          = findViewById(R.id.input_layout_first_name_contacts_id);
        mLastNameTextInputLayout           = findViewById(R.id.input_layout_last_name_contacts_id);
        mMobileNumberTextInputLayout       = findViewById(R.id.input_layout_mobile_number_contacts_id);
        mWorkNumberTextInputLayout         = findViewById(R.id.input_layout_work_number_contacts_id);
        mHomeNumberTextInputLayout         = findViewById(R.id.input_layout_home_number_contacts_id);
        mMainNumberTextInputLayout         = findViewById(R.id.input_layout_main_number_contacts_id);
        mEmailTextInputLayout              = findViewById(R.id.input_layout_email_contacts_id);

        mSaveBtn                           = findViewById(R.id.save_button_contacts_id);
        mCancelBtn                         = findViewById(R.id.cancel_button_contacts_id);

        Toolbar toolbar                    = findViewById(R.id.contacts_toolbar);
        setSupportActionBar(toolbar);

        mSaveBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        mFirstNameEditText.addTextChangedListener(watcher);
        mLastNameEditText.addTextChangedListener(watcher);
        mMobileNumberAutoCompleteTextView.addTextChangedListener(watcher);
        mEmailEditText.addTextChangedListener(watcher);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
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
                setDifferentPhoneNumberTypes();
                getSupportActionBar().setTitle(R.string.edit_contact);
            break;
            case VIEW_CONTACT:
                mContactsBinding.setUser(mUser);
                setDifferentPhoneNumberTypes();
                disableFieldsWhenViewing();
                getSupportActionBar().setTitle(R.string.view_contact);
                mSaveBtn.setVisibility(View.GONE);
                hideEmptyFields();
            break;
        }
    }
    private void setDifferentPhoneNumberTypes(){
        if (!mUser.getMainNumberList().isEmpty()){
            mMainNumberAutoCompleteTextView.setText(mUser.getMainNumberList().get(0));
            mMainNumberAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this,R.layout.dropdown_phone_number_menu_item,mUser.getMainNumberList()));
        }

        if (!mUser.getMobileNumberList().isEmpty()){
            mMobileNumberAutoCompleteTextView.setText(mUser.getMobileNumberList().get(0));
            mMobileNumberAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this,R.layout.dropdown_phone_number_menu_item,mUser.getMobileNumberList()));
        }
        if (!mUser.getHomeNumberList().isEmpty()){
            mHomeNumberAutoCompleteTextView.setText(mUser.getHomeNumberList().get(0));
            mHomeNumberAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this,R.layout.dropdown_phone_number_menu_item,mUser.getHomeNumberList()));
        }

        if (!mUser.getWorkNumberList().isEmpty()){
            mWorkNumberAutoCompleteTextView.setText(mUser.getWorkNumberList().get(0));
            mWorkNumberAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this,R.layout.dropdown_phone_number_menu_item,mUser.getWorkNumberList()));
        }
    }

    private void checkIfFieldsAreEmpty(){
        if (mFirstNameEditText.getText().toString().isEmpty()    ||
            mLastNameEditText.getText().toString().isEmpty()     ||
            mEmailEditText.getText().toString().isEmpty()        ||
            mMobileNumberAutoCompleteTextView.getText().toString().isEmpty()){
            mSaveBtn.setEnabled(false);
        }
        else
            mSaveBtn.setEnabled(true);
    }

    private void checkErrorsInFields(Editable editable){
        if (editable.length()>mFirstNameTextInputLayout.getCounterMaxLength()   &&
            mFirstNameEditText.hasFocus()){
            mFirstNameTextInputLayout.setError( getString(R.string.input_text_error_message)   +
            mFirstNameTextInputLayout.getCounterMaxLength());
        }
        else
            mFirstNameTextInputLayout.setError(null);

        if (editable.length()>mLastNameTextInputLayout.getCounterMaxLength()    &&
            mLastNameEditText.hasFocus()){
            mLastNameTextInputLayout.setError( getString(R.string.input_text_error_message)    +
            mLastNameTextInputLayout.getCounterMaxLength());
        }
        else
            mLastNameTextInputLayout.setError(null);

        if (editable.length()>mMobileNumberTextInputLayout.getCounterMaxLength()  &&
            mMobileNumberAutoCompleteTextView.hasFocus()){
            mMobileNumberTextInputLayout.setError( getString(R.string.input_text_error_message)  +
            mMobileNumberTextInputLayout.getCounterMaxLength());
        }
        else
            mMobileNumberTextInputLayout.setError(null);

        if (editable.length()>mEmailTextInputLayout.getCounterMaxLength()        &&
            mEmailTextInputLayout.hasFocus()){
            mEmailTextInputLayout.setError( getString(R.string.input_text_error_message)        +
            mEmailTextInputLayout.getCounterMaxLength());
        }
        else
            mEmailTextInputLayout.setError(null);
    }

    private void hideEmptyFields(){
        if (mUser.getFirstName() == null || mUser.getFirstName().equalsIgnoreCase(" "))
            mFirstNameTextInputLayout.setVisibility(View.GONE);

        if (mUser.getLastName() == null || mUser.getLastName().equalsIgnoreCase(" "))
            mLastNameTextInputLayout.setVisibility(View.GONE);

        if (mUser.getMobileNumberList() == null || mUser.getMobileNumberList().isEmpty())
            mMobileNumberTextInputLayout.setVisibility(View.GONE);

        if (mUser.getWorkNumberList() == null || mUser.getWorkNumberList().isEmpty())
            mWorkNumberTextInputLayout.setVisibility(View.GONE);

        if (mUser.getHomeNumberList() == null || mUser.getHomeNumberList().isEmpty())
            mHomeNumberTextInputLayout.setVisibility(View.GONE);

        if (mUser.getMainNumberList() == null || mUser.getMainNumberList().isEmpty())
            mMainNumberTextInputLayout.setVisibility(View.GONE);

        if (mUser.getEmail() == null || mUser.getEmail().equalsIgnoreCase(" "))
            mEmailTextInputLayout.setVisibility(View.GONE);
    }

    private void disableFieldsWhenViewing(){
        mFirstNameEditText.setEnabled(false);
        mLastNameEditText.setEnabled(false);
        mMobileNumberAutoCompleteTextView.setEnabled(false);
        mWorkNumberAutoCompleteTextView.setEnabled(false);
        mHomeNumberAutoCompleteTextView.setEnabled(false);
        mMainNumberAutoCompleteTextView.setEnabled(false);
        mEmailEditText.setEnabled(false);
    }
}
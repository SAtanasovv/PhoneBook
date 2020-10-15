package com.satanasov.phonebook.view;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.satanasov.phonebook.databinding.ActivityContactsBinding;
import com.satanasov.phonebook.db.DataBaseQueries;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.model.EmailModel;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.satanasov.phonebook.R;
import java.util.ArrayList;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    private ActivityContactsBinding       mContactsBinding;

    private ImageView                     mContactImage;
    private TextInputEditText             mFirstNameEditText;
    private TextInputEditText             mLastNameEditText;

    private TextInputLayout    mFirstNameTextInputLayout;
    private TextInputLayout    mLastNameTextInputLayout;

    private Button             mSaveBtn;
    private Button             mCancelBtn;

    private ContactModel       mContact;
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
                onBackPressed();
            break;

            case R.id.save_button_contacts_id:
                saveContactToDB();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        mContactImage               = findViewById(R.id.contact_image_view);
        mFirstNameEditText          = findViewById(R.id.first_name_edit_text_contacts_id);
        mLastNameEditText           = findViewById(R.id.last_name_edit_text_contacts_id);

        mFirstNameTextInputLayout   = findViewById(R.id.input_layout_first_name_contacts_id);
        mLastNameTextInputLayout    = findViewById(R.id.input_layout_last_name_contacts_id);

        mSaveBtn                    = findViewById(R.id.save_button_contacts_id);
        mCancelBtn                  = findViewById(R.id.cancel_button_contacts_id);

        Toolbar toolbar             = findViewById(R.id.contacts_toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mSaveBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

        mFirstNameEditText.addTextChangedListener(watcher);
        mLastNameEditText.addTextChangedListener(watcher);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            mOption = (ChangeOptions) bundle.getSerializable(Utils.INTENT_EXTRA_OPTION);
            mContact   = bundle.getParcelable(Utils.INTENT_USER_DETAILS);
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
                mContactsBinding.setUser(mContact);
                mContactImage.setImageBitmap(mContact.getImageId());
                getSupportActionBar().setTitle(R.string.edit_contact);

                setDifferentPhoneNumberTypes();
            break;

            case VIEW_CONTACT:
                mContactsBinding.setUser(mContact);
                mContactImage.setImageBitmap(mContact.getImageId());
                mSaveBtn.setVisibility(View.GONE);
                getSupportActionBar().setTitle(R.string.view_contact);

                setDifferentPhoneNumberTypes();
                disableFieldsWhenViewing();
                hideEmptyFields();
            break;
        }
    }

    private void setDifferentPhoneNumberTypes(){
        // Set existing contacts for view/edit
    }

    private void checkIfFieldsAreEmpty(){
        if (mFirstNameEditText.getText().toString().equalsIgnoreCase(" ")){

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
    }

    private void hideEmptyFields(){
        if (mContact.getFirstName() == null || mContact.getFirstName().equalsIgnoreCase(" "))
            mFirstNameTextInputLayout.setVisibility(View.GONE);

        if (mContact.getLastName() == null || mContact.getLastName().equalsIgnoreCase(" "))
            mLastNameTextInputLayout.setVisibility(View.GONE);
    }

    private void disableFieldsWhenViewing(){
        mFirstNameEditText.setEnabled(false);
        mLastNameEditText.setEnabled(false);
    }

    private ArrayList<PhoneNumberModel> getAllPhoneNumbers(){
        ArrayList<PhoneNumberModel> contactPhoneNumberModelsList = new ArrayList<>();
        // Get new contact phoneNumbers
        return contactPhoneNumberModelsList;
    }

    private ArrayList<EmailModel> getAllEmails(){
        ArrayList<EmailModel> contactEmailModelList = new ArrayList<>();
        // Get new contact emails
        return contactEmailModelList;
    }

    private void saveContactToDB(){
        DataBaseQueries dataBaseQueries = new DataBaseQueries();
        dataBaseQueries.storeContact(this, new ContactModel(mFirstNameEditText.getText().toString(),mLastNameEditText.getText().toString(),getAllPhoneNumbers(),getAllEmails()));
    }
}
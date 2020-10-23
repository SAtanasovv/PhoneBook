package com.satanasov.phonebook.view;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.satanasov.phonebook.helpers.ContactsData;
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

    private ContactsData             mContactsData  = new ContactsData(this);
    private ActivityContactsBinding  mContactsBinding;

    private ImageView                mContactImage;
    private TextInputEditText        mFirstNameEditText;
    private TextInputEditText        mLastNameEditText;
    private Spinner                  mPhoneNumberTypeSpinner;
    private TextInputEditText        mPhoneNumberEditText;
    private Spinner                  mEmailTypeSpinner;
    private TextInputEditText        mEmailEditText;

    private TextInputLayout    mFirstNameTextInputLayout;
    private TextInputLayout    mLastNameTextInputLayout;
    private LinearLayout       mPhoneNumberLayout;
    private LinearLayout       mEmailLayout;

    private ImageButton        mInsertPhoneNumberRow;
    private ImageButton        mInsertEmailRow;
    private ImageButton        mDeletePhoneNumberRow;
    private ImageButton        mDeleteEmailRow;
    private Button             mSaveBtn;
    private Button             mCancelBtn;

    private ContactModel       mContact;
    private ChangeOptions      mOption;

    ArrayList<PhoneNumberModel> mPhoneNumbersList = new ArrayList<>();
    ArrayList<EmailModel>       mEmailList        = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsBinding = DataBindingUtil.setContentView(this,R.layout.activity_contacts);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.insert_phone_number_row:
                insertPhoneNumberRow();
            break;

            case R.id.insert_email_row:
                insertEmailRow();
            break;

            case R.id.cancel_button_contacts_id:
                onBackPressed();
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
        mContactImage               = findViewById(R.id.contact_image_view);
        mFirstNameEditText          = findViewById(R.id.first_name_edit_text_contacts_id);
        mLastNameEditText           = findViewById(R.id.last_name_edit_text_contacts_id);

        mPhoneNumberTypeSpinner     = findViewById(R.id.phone_number_type_spinner);
        mPhoneNumberEditText        = findViewById(R.id.phone_number_field_edit_text);
        mInsertPhoneNumberRow       = findViewById(R.id.insert_phone_number_row);

        mEmailTypeSpinner           = findViewById(R.id.email_type_spinner);
        mEmailEditText              = findViewById(R.id.email_field_edit_text);
        mInsertEmailRow             = findViewById(R.id.insert_email_row);

        mPhoneNumberLayout          = findViewById(R.id.phone_number_layout);
        mEmailLayout                = findViewById(R.id.email_layout);
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

        mCancelBtn.setOnClickListener(this);
        mInsertPhoneNumberRow.setOnClickListener(this);
        mInsertEmailRow.setOnClickListener(this);

        mFirstNameEditText.addTextChangedListener(watcher);
        mLastNameEditText.addTextChangedListener(watcher);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            mOption     = (ChangeOptions) bundle.getSerializable(Utils.INTENT_EXTRA_OPTION);
            mContact    = bundle.getParcelable(Utils.INTENT_USER_DETAILS);
            changeOptions(mOption);
        }
    }

    private void changeOptions(ChangeOptions option){
        switch(option){

            case ADD_CONTACT:
                mSaveBtn.setEnabled(false);
                mSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveContactToDB();
                    }
                });
                getSupportActionBar().setTitle(R.string.add_contact);
            break;

            case EDIT_CONTACT:
                mContactsBinding.setUser(mContact);
                mContactImage.setImageBitmap(mContact.getImageId());
                setPhoneNumbersAndEmails();
                mSaveBtn.setText(R.string.edit_contact);
                mSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateContact();
                    }
                });
                getSupportActionBar().setTitle(R.string.edit_contact);
             break;

            case VIEW_CONTACT:
                mContactsBinding.setUser(mContact);
                mContactImage.setImageBitmap(mContact.getImageId());
                mSaveBtn.setVisibility(View.GONE);
                getSupportActionBar().setTitle(R.string.view_contact);

                disableFieldsWhenViewing();
                hideEmptyFields();
            break;
        }
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

    private void insertPhoneNumberRow(){
       LayoutInflater rowInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
       View phoneNumberRow        = rowInflater.inflate(R.layout.activity_contact_row,null);

       mDeletePhoneNumberRow = phoneNumberRow.findViewById(R.id.remove_view);
       mDeletePhoneNumberRow.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               mPhoneNumberLayout.removeView((LinearLayout)v.getParent());
           }
       });
       mPhoneNumberLayout.addView(phoneNumberRow);
    }

    private void insertEmailRow(){
        LayoutInflater rowInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View phoneNumberRow        = rowInflater.inflate(R.layout.activity_contact_row,null);

        mDeleteEmailRow = phoneNumberRow.findViewById(R.id.remove_view);
        mDeleteEmailRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEmailLayout.removeView((LinearLayout)v.getParent());
            }
        });
        mEmailLayout.addView(phoneNumberRow);
    }

    private ArrayList<PhoneNumberModel> getAllPhoneNumbers(){
        ArrayList<PhoneNumberModel> contactPhoneNumberModelsList = new ArrayList<>();

        PhoneNumberModel phoneNumberModel = new PhoneNumberModel(mPhoneNumberEditText.getText().toString(),mContactsData.getSpinnerTypeID(mPhoneNumberTypeSpinner.getSelectedItem().toString()));
        contactPhoneNumberModelsList.add(phoneNumberModel);

            for (int i = 1; i < mPhoneNumberLayout.getChildCount(); i++){
                View phoneView = mPhoneNumberLayout.getChildAt(i);

                Spinner typeSpinner         = phoneView.findViewById(R.id.type_spinner);
                TextInputEditText phoneText = phoneView.findViewById(R.id.type_field_edit_text);

                if (!phoneText.getText().toString().isEmpty()){

                phoneNumberModel = new PhoneNumberModel(phoneText.getText().toString(),mContactsData.getSpinnerTypeID(typeSpinner.getSelectedItem().toString()));
                contactPhoneNumberModelsList.add(phoneNumberModel);
                }
            }
        return contactPhoneNumberModelsList;
    }

    private ArrayList<EmailModel> getAllEmails(){
        ArrayList<EmailModel> contactEmailModelList = new ArrayList<>();

        EmailModel emailModel = new EmailModel(mEmailEditText.getText().toString(), mContactsData.getSpinnerTypeID(mEmailTypeSpinner.getSelectedItem().toString()));
        contactEmailModelList.add(emailModel);

            for (int i = 1; i < mEmailLayout.getChildCount(); i++){
                View emailView = mEmailLayout.getChildAt(i);

                Spinner typeSpinner         = emailView.findViewById(R.id.type_spinner);
                TextInputEditText emailText = emailView.findViewById(R.id.type_field_edit_text);

                if (!emailText.getText().toString().isEmpty()){

                    emailModel = new EmailModel(emailText.getText().toString(),mContactsData.getSpinnerTypeID(typeSpinner.getSelectedItem().toString()));
                    contactEmailModelList.add(emailModel);
                }
             }
            return contactEmailModelList;
    }

    private void saveContactToDB(){
        DataBaseQueries dataBaseQueries = new DataBaseQueries();
        ContactModel contactModel       = new ContactModel(mFirstNameEditText.getText().toString(),mLastNameEditText.getText().toString(),getAllPhoneNumbers(),getAllEmails(),true);

        contactModel.setDataBaseContact(true);

        dataBaseQueries.storeContact(this, contactModel);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Utils.RETURN_CONTACT_TO_MAIN_ACTIVITY_ADD,contactModel);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void setPhoneNumbersAndEmails(){
        ArrayList<PhoneNumberModel> phoneNumberModelList = mContact.getPhoneNumberModelList();
        ArrayList<EmailModel> emailModelList             = mContact.getEmailModelList();

        if (!phoneNumberModelList.isEmpty()) {
            mPhoneNumberTypeSpinner.setSelection(Math.toIntExact(phoneNumberModelList.get(0).getPhoneNumberType()));
            mPhoneNumberEditText.setText(phoneNumberModelList.get(0).getPhoneNumber());

            for (int i = 1; i < phoneNumberModelList.size(); i++) {
                LayoutInflater rowInflater = this.getLayoutInflater();
                View phoneNumberRow        = rowInflater.inflate(R.layout.activity_contact_row,null);

                Spinner typeSpinner         = phoneNumberRow.findViewById(R.id.type_spinner);
                TextInputEditText textField = phoneNumberRow.findViewById(R.id.type_field_edit_text);
                ImageButton removeRow       = phoneNumberRow.findViewById(R.id.remove_view);

                typeSpinner.setSelection(Math.toIntExact(phoneNumberModelList.get(i).getPhoneNumberType()));
                textField.setText(phoneNumberModelList.get(i).getPhoneNumber());
                removeRow.setTag(phoneNumberModelList.get(i));
                removeRow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PhoneNumberModel phoneNumber = (PhoneNumberModel) v.getTag();
                        phoneNumber.setDBOperationType(Utils.DELETE);
                        mPhoneNumbersList.add(phoneNumber);
                        mPhoneNumberLayout.removeView((LinearLayout) v.getParent());
                    }
                });
                mPhoneNumberLayout.addView(phoneNumberRow);
            }
        }
        if (!emailModelList.isEmpty()) {
            mEmailTypeSpinner.setSelection(Math.toIntExact(emailModelList.get(0).getEmailType()));
            mEmailEditText.setText(emailModelList.get(0).getEmail());

            for (int i = 1; i < emailModelList.size(); i++) {
                LayoutInflater rowInflater = this.getLayoutInflater();
                View emailRow              = rowInflater.inflate(R.layout.activity_contact_row,null);

                Spinner typeSpinner         = emailRow.findViewById(R.id.type_spinner);
                TextInputEditText textField = emailRow.findViewById(R.id.type_field_edit_text);
                ImageButton removeRow       = emailRow.findViewById(R.id.remove_view);

                typeSpinner.setSelection(Math.toIntExact(emailModelList.get(i).getEmailType()));
                textField.setText(emailModelList.get(i).getEmail());
                removeRow.setTag(emailModelList.get(i));
                removeRow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EmailModel email = (EmailModel) v.getTag();
                        email.setDBOperationType(Utils.DELETE);
                        mEmailList.add(email);
                        mEmailLayout.removeView((LinearLayout) v.getParent());
                    }
                });
                mEmailLayout.addView(emailRow);
            }
        }
    }

    private void updateContact(){
        DataBaseQueries dataBaseQueries = new DataBaseQueries();


        if (!mFirstNameEditText.getText().toString().equals(mContact.getFirstName()) && !mFirstNameEditText.getText().toString().equals("")){

            mContact.setFirstName(mFirstNameEditText.getText().toString());
            mContact.setDBOperationType(Utils.UPDATE);
        }

        if (!mLastNameEditText.getText().toString().equals(mContact.getLastName())){

            mContact.setLastName(mLastNameEditText.getText().toString());
            mContact.setDBOperationType(Utils.UPDATE);
        }

        if (!mPhoneNumberEditText.getText().toString().equals(mContact.getPhoneNumberModelList().get(0)) && !mPhoneNumberEditText.getText().toString().equals("") ){

            PhoneNumberModel phoneNumberModel = mContact.getPhoneNumberModelList().get(0);
            phoneNumberModel.setPhoneNumber(mPhoneNumberEditText.getText().toString());
            phoneNumberModel.setDBOperationType(Utils.UPDATE);
            mPhoneNumbersList.add(phoneNumberModel);
        }

        if (!mEmailEditText.getText().toString().equals(mContact.getEmailModelList().get(0)) && !mEmailEditText.getText().toString().equals("")){

            EmailModel emailModel = mContact.getEmailModelList().get(0);
            emailModel.setEmail(mEmailEditText.getText().toString());
            emailModel.setDBOperationType(Utils.UPDATE);
            mEmailList.add(emailModel);
        }
        mContact.setPhoneNumberModelList(defineDBOperationsForPhoneNumbers());
        mContact.setEmailModelList(defineDBOperationsForEmails());
        dataBaseQueries.updateContact(this,mContact);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Utils.RETURN_CONTACT_TO_MAIN_ACTIVITY_EDIT,mContact);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private ArrayList<PhoneNumberModel> defineDBOperationsForPhoneNumbers(){
        for (int i = 1; i < mPhoneNumberLayout.getChildCount(); i++){
            View phoneView = mPhoneNumberLayout.getChildAt(i);

            TextInputEditText phoneEditText = phoneView.findViewById(R.id.type_field_edit_text);
            Spinner typeSpinner             = phoneView.findViewById(R.id.type_spinner);
            ImageButton removeRow           = phoneView.findViewById(R.id.remove_view);

            if (removeRow.getTag() == null){
                if (!phoneEditText.getText().toString().equals("")){

                    PhoneNumberModel phoneNumber = new PhoneNumberModel(phoneEditText.getText().toString(),mContactsData.getSpinnerTypeID(typeSpinner.getSelectedItem().toString()),Utils.INSERT);
                    mPhoneNumbersList.add(phoneNumber);
                }
            }
            else {
                PhoneNumberModel phoneNumber = (PhoneNumberModel) removeRow.getTag();

                if (!phoneNumber.getPhoneNumber().equals(phoneEditText.getText().toString()) && !phoneEditText.getText().toString().equals("")){

                    phoneNumber.setPhoneNumber(phoneEditText.getText().toString());
                    phoneNumber.setDBOperationType(Utils.UPDATE);
                    mPhoneNumbersList.add(phoneNumber);
                }
            }
        }
        return mPhoneNumbersList;
    }

    private ArrayList<EmailModel> defineDBOperationsForEmails(){

        for (int i = 1; i < mEmailLayout.getChildCount(); i++){
            View emailView = mEmailLayout.getChildAt(i);

            TextInputEditText emailEditText = emailView.findViewById(R.id.type_field_edit_text);
            Spinner typeSpinner             = emailView.findViewById(R.id.type_spinner);
            ImageButton removeRow           = emailView.findViewById(R.id.remove_view);

            if (removeRow.getTag() == null){
                if (!emailEditText.getText().toString().equals("")){

                    EmailModel email = new EmailModel(emailEditText.getText().toString(),mContactsData.getSpinnerTypeID(typeSpinner.getSelectedItem().toString()),Utils.INSERT);
                    mEmailList.add(email);
                }
            }
            else {
                EmailModel emailModel = (EmailModel) removeRow.getTag();

                if (!emailModel.getEmail().equals(emailEditText.getText().toString()) && !emailEditText.getText().toString().equals("")){

                    emailModel.setEmail(emailEditText.getText().toString());
                    emailModel.setDBOperationType(Utils.UPDATE);
                    mEmailList.add(emailModel);
                }
            }
        }
        return mEmailList;
    }
}
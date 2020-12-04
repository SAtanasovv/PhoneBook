package com.satanasov.phonebook.view;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.model.EmailModel;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.presenter.ContactActivityPresenter;
import com.satanasov.phonebook.presenter.ContactActivityView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactsActivity extends BaseActivity implements View.OnClickListener , ContactActivityView {

    private ContactsData             mContactsData  = new ContactsData(this);
    private ActivityContactsBinding  mContactsBinding;
    private ContactActivityPresenter mPresenter;

    private ImageView                mContactImage;
    private TextInputEditText        mFirstNameEditText;
    private TextInputEditText        mLastNameEditText;
    private Spinner                  mPhoneNumberTypeSpinner;
    private TextInputEditText        mPhoneNumberEditText;
    private Spinner                  mEmailTypeSpinner;
    private TextInputEditText        mEmailEditText;

    private TextInputLayout    mFirstNameTextInputLayout;
    private TextInputLayout    mLastNameTextInputLayout;
    private TextInputLayout    mEmailTextInputLayout;
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

    private boolean            mErrorInFirstName;
    private boolean            mErrorInLastName;

    ArrayList<PhoneNumberModel> mPhoneNumbersList = new ArrayList<>();
    ArrayList<EmailModel>       mEmailList        = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsBinding = DataBindingUtil.setContentView(this,R.layout.activity_contacts);
        mPresenter       = new ContactActivityPresenter(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        mPresenter.unSubscribe();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
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

            checkErrorsInFields(editable);
            disableSaveButtonIfErrorsFound();
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
        mEmailTextInputLayout       = findViewById(R.id.email_type_field_layout);
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
        mPhoneNumberEditText.addTextChangedListener(watcher);
        mEmailEditText.addTextChangedListener(watcher);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            this.mOption     = (ChangeOptions) bundle.getSerializable(Utils.INTENT_EXTRA_OPTION);
            this.mContact    = bundle.getParcelable(Utils.INTENT_USER_DETAILS);
            changeOptions(mOption);
        }
    }

    private void changeOptions(ChangeOptions option){
        switch(option){

            case ADD_CONTACT:
                mPresenter.mIsContactForEdit = false;
            break;

            case EDIT_CONTACT:
                mPresenter.mIsContactForEdit = true;
            break;
        }
    }

    private boolean areFieldsEmpty(){
        if  (!mFirstNameEditText.getText().toString().equalsIgnoreCase("") )
            return false;
        else
            return true;
    }

    private void checkErrorsInFields(Editable editable){
        if (editable.length() > mFirstNameTextInputLayout.getCounterMaxLength() && mFirstNameEditText.hasFocus()){

            mFirstNameTextInputLayout.setError(getString(R.string.input_text_error_message) + mFirstNameTextInputLayout.getCounterMaxLength());
            mErrorInFirstName = true;
        }
        else{
            mFirstNameTextInputLayout.setError(null);
            mErrorInFirstName = false;
        }

        if (editable.length() > mLastNameTextInputLayout.getCounterMaxLength() && mLastNameEditText.hasFocus()){

            mLastNameTextInputLayout.setError( getString(R.string.input_text_error_message) + mLastNameTextInputLayout.getCounterMaxLength());
            mErrorInLastName = true;
        }
        else{
            mLastNameTextInputLayout.setError(null);
            mErrorInLastName = false;
        }
    }

    private boolean isEmailValid(){
         String  regEx     = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
         Pattern pattern   = Pattern.compile(regEx);

        if (!mEmailEditText.getText().toString().equalsIgnoreCase(" ") && !mEmailEditText.getText().toString().equalsIgnoreCase("")) {

            Matcher matcher = pattern.matcher(mEmailEditText.getText().toString());

            if (!matcher.matches()) {
                mEmailTextInputLayout.setError(getString(R.string.input_text_email_error_message));
                return false;
            } else
                mEmailTextInputLayout.setError(null);
        }

        if (mEmailLayout.getChildCount() > 0){
            for (int i = 1; i < mEmailLayout.getChildCount(); i++){

                View emailView = mEmailLayout.getChildAt(i);

                TextInputEditText emailText       = emailView.findViewById(R.id.type_field_edit_text);
                TextInputLayout   emailTextLayout = emailView.findViewById(R.id.type_field_layout);

                Matcher matcherChild              = pattern.matcher(emailText.getText());

                if (!mEmailEditText.getText().toString().equalsIgnoreCase("")) {

                    if (!matcherChild.matches()){

                        emailTextLayout.setError(getString(R.string.input_text_email_error_message));
                        return false;
                    }
                    else
                    emailTextLayout.setError(null);
                }
            }
        }
        mEmailTextInputLayout.setError(null);
        return true;
    }


    private void disableSaveButtonIfErrorsFound(){
        if (!areFieldsEmpty() && !mErrorInFirstName && !mErrorInLastName && isEmailValid()){
            mSaveBtn.setEnabled(true);
        }
        else
            mSaveBtn.setEnabled(false);
    }

    private void insertPhoneNumberRow(){
       LayoutInflater rowInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
       View phoneNumberRow        = rowInflater.inflate(R.layout.activity_contact_row,null);

       TextInputEditText textField = phoneNumberRow.findViewById(R.id.type_field_edit_text);
       mDeletePhoneNumberRow       = phoneNumberRow.findViewById(R.id.remove_view);

       textField.setInputType(InputType.TYPE_CLASS_NUMBER);
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

        final TextInputEditText emailView = phoneNumberRow.findViewById(R.id.type_field_edit_text);
        emailView.addTextChangedListener(watcher);
        mDeleteEmailRow = phoneNumberRow.findViewById(R.id.remove_view);

        emailView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mDeleteEmailRow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextInputEditText editText = emailView.findViewById(R.id.type_field_edit_text);
                editText.setText(R.string.dummy_email);
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

        ContactModel contactModel  = new ContactModel(mFirstNameEditText.getText().toString(),mLastNameEditText.getText().toString(),getAllPhoneNumbers(),getAllEmails(),true);
        mPresenter.saveContactToDB(contactModel);
    }

    private void setPhoneNumbersAndEmails(){
        ArrayList<PhoneNumberModel> phoneNumberModelList = mContact.getPhoneNumberModelList();
        ArrayList<EmailModel> emailModelList             = mContact.getEmailModelList();

        if (!phoneNumberModelList.isEmpty()) {
            mPhoneNumberTypeSpinner.setSelection(Math.toIntExact(phoneNumberModelList.get(0).getPhoneNumberType()));
            mPhoneNumberEditText.setText(phoneNumberModelList.get(0).getPhoneNumber());

            for (int i = 1; i < phoneNumberModelList.size(); i++) {
                PhoneNumberModel phoneNumberModel = phoneNumberModelList.get(i);

                LayoutInflater rowInflater  = this.getLayoutInflater();
                View phoneNumberRow         = rowInflater.inflate(R.layout.activity_contact_row,null);

                Spinner typeSpinner         = phoneNumberRow.findViewById(R.id.type_spinner);
                TextInputEditText textField = phoneNumberRow.findViewById(R.id.type_field_edit_text);
                ImageButton removeRow       = phoneNumberRow.findViewById(R.id.remove_view);

                textField.setInputType(InputType.TYPE_CLASS_NUMBER);
                typeSpinner.setSelection(Math.toIntExact(phoneNumberModel.getPhoneNumberType()));
                textField.setText(phoneNumberModel.getPhoneNumber());
                removeRow.setTag(phoneNumberModel);
                removeRow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        PhoneNumberModel phoneNumber = (PhoneNumberModel) v.getTag();
                        phoneNumber.setDBOperationType(Utils.DELETE);
                        mPhoneNumbersList.add(phoneNumber);
                        mPhoneNumberLayout.removeView((LinearLayout) v.getParent());
                    }
                });

                if (phoneNumberModel.getID() == null){
                    typeSpinner.setEnabled(false);
                    textField.setEnabled(false);
                    removeRow.setVisibility(View.INVISIBLE);
                }
                else {
                    typeSpinner.setEnabled(true);
                    textField.setEnabled(true);
                    removeRow.setVisibility(View.VISIBLE);
                }
                mPhoneNumberLayout.addView(phoneNumberRow);
            }
        }
        if (!emailModelList.isEmpty()) {
            mEmailTypeSpinner.setSelection(Math.toIntExact(emailModelList.get(0).getEmailType()));
            mEmailEditText.setText(emailModelList.get(0).getEmail());

            for (int i = 1; i < emailModelList.size(); i++) {
                EmailModel emailModel = emailModelList.get(i);

                LayoutInflater rowInflater  = this.getLayoutInflater();
                View emailRow               = rowInflater.inflate(R.layout.activity_contact_row,null);

                Spinner typeSpinner         = emailRow.findViewById(R.id.type_spinner);
                final TextInputEditText textField = emailRow.findViewById(R.id.type_field_edit_text);
                ImageButton removeRow       = emailRow.findViewById(R.id.remove_view);

                textField.addTextChangedListener(watcher);
                textField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                typeSpinner.setSelection(Math.toIntExact(emailModel.getEmailType()));
                textField.setText(emailModel.getEmail());
                removeRow.setTag(emailModel);
                removeRow.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        textField.setText(R.string.dummy_email);
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

        mPresenter.updateContactInDB(mContact);

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

    @Override
    public void returnToMainActivity() {
        onBackPressed();
    }

    @Override
    public void setContactDetailsForInsert() {
        mSaveBtn.setEnabled(false);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContactToDB();
            }
        });

        getSupportActionBar().setTitle(R.string.add_contact);
    }

    @Override
    public void setContactDetailsForEdit() {
        mContactsBinding.setUser(mContact);
        mContactImage.setImageBitmap(mContact.getImageId());
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });

        setPhoneNumbersAndEmails();
        getSupportActionBar().setTitle(R.string.edit_contact);
    }

}
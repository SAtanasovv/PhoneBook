package com.satanasov.phonebook.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.satanasov.phonebook.Model.User;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.Utils.ChangeOptions;

public class ContactsActivity extends BaseActivity {

    private                         TextView        firstNameTextView;
    private                         TextView        lastNameTextView;
    private                         TextView        phoneNumberTextView;
    private                         TextView        emailTextView;

    private                         Button          saveBtn;
    private                         Button          cancelBtn;

    private                         User            user;
    private                         int             option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        firstNameTextView = findViewById(R.id.first_name_edit_text_contacts_id);
        lastNameTextView = findViewById(R.id.last_name_edit_text_contacts_id);
        phoneNumberTextView = findViewById(R.id.phone_number_edit_text_contacts_id);
        emailTextView = findViewById(R.id.email_edit_text_contacts_id);

        saveBtn = findViewById(R.id.save_button_contacts_id);
        cancelBtn = findViewById(R.id.cancel_button_contacts_id);



        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            option = bundle.getInt("changeOption");
            user = bundle.getParcelable("contact");
            changeOptions(option);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this, MainActivity.class));
            }
        });

        firstNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!areFieldsEmpty() && option == 1 ){
                    enableButtons();
                }
                else
                    disableButtons();

            }
        });

        lastNameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!areFieldsEmpty() && option == 1 ){
                    enableButtons();
                }
                else
                    disableButtons();

            }
        });

        emailTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!areFieldsEmpty() && option == 1 ){
                    enableButtons();
                }
                else
                    disableButtons();
            }
        });

        phoneNumberTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!areFieldsEmpty() && option == 1 ){
                    enableButtons();
                }
                else
                    disableButtons();
            }
        });

    }


    private void changeOptions(int option){


        switch(option){
            case 1:
                disableButtons();
                break;
            case 2:
                setUser();
                break;
            case 3:
                setUser();
                disableFields();
                disableButtons();
                break;

        }

    }

    private boolean areFieldsEmpty(){
        if(firstNameTextView.getText().toString().isEmpty())
            return true;
        else if (lastNameTextView.getText().toString().isEmpty())
            return true;
        else if (emailTextView.getText().toString().isEmpty())
            return true;
        else if (phoneNumberTextView.getText().toString().isEmpty())
            return true;
        else

        return false;
    }


    private void disableButtons(){
        saveBtn.setVisibility(View.GONE);
       // cancelBtn.setVisibility(View.GONE);

    }

    private void enableButtons(){
        saveBtn.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);

    }

    private void disableFields(){
        firstNameTextView.setEnabled(false);
        lastNameTextView.setEnabled(false);
        phoneNumberTextView.setEnabled(false);
        emailTextView.setEnabled(false);
    }
    private void setUser(){
        firstNameTextView.setText(user.getFirstName());
        lastNameTextView.setText(user.getLastName());
        phoneNumberTextView.setText(user.getPhoneNumber());
        emailTextView.setText(user.getEmail());
    }


}
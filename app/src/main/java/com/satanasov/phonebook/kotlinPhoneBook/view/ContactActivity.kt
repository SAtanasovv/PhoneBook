package com.satanasov.phonebook.kotlinPhoneBook.view
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.satanasov.phonebook.R
import com.satanasov.phonebook.databinding.ActivityContactsBinding
import com.satanasov.phonebook.globalData.Utils
import com.satanasov.phonebook.globalData.Utils.ChangeOptions
import com.satanasov.phonebook.helpers.ContactsData
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel
import com.satanasov.phonebook.kotlinPhoneBook.model.EmailModel
import com.satanasov.phonebook.kotlinPhoneBook.model.PhoneNumberModel
import com.satanasov.phonebook.kotlinPhoneBook.presenter.ContactActivityPresenter
import com.satanasov.phonebook.kotlinPhoneBook.presenter.ContactActivityView
import kotlinx.android.synthetic.main.activity_contact_row.view.*
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactActivity : BaseActivity(), ContactActivityView{
    private var mContactsData: ContactsData = ContactsData(this)

    private lateinit var mContactsBinding:  ActivityContactsBinding
    private lateinit var mPresenter:        ContactActivityPresenter

    private var mContact:  ContactModel? = null

    private var mErrorInFirstName = false
    private var mErrorInLastName  = false

    private var mPhoneNumbersList   = ArrayList<PhoneNumberModel>()
    private var mEmailList          = ArrayList<EmailModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContactsBinding = DataBindingUtil.setContentView(this, R.layout.activity_contacts)
        mPresenter       = ContactActivityPresenter(this)
        init()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.subscribe()
    }

    override fun onPause() {
        mPresenter.unSubscribe()
        super.onPause()
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if ( item?.itemId == android.R.id.home )
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun init(){
        setSupportActionBar(contactsToolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val bundle   = intent.extras
        if (bundle  != null){
            mContact = bundle.getParcelable(Utils.INTENT_USER_DETAILS)
            changeOptions(bundle.getSerializable(Utils.INTENT_EXTRA_OPTION) as ChangeOptions)
        }
        insertPhoneNumberRow.setOnClickListener  { insertPhoneNumberRow() }
        insertEmailRow.setOnClickListener        { insertEmailRow() }
        cancelButton.setOnClickListener          { onBackPressed() }

        firstNameEditText.addTextChangedListener(textWatcher)
        phoneNumberFieldEditText.addTextChangedListener(textWatcher)
        emailFieldEditText.addTextChangedListener(textWatcher)
    }

    private val textWatcher: TextWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            checkErrorsInFields(s)
        }
    }

    private fun changeOptions(changeOptions: ChangeOptions){
        when(changeOptions){
            ChangeOptions.ADD_CONTACT  -> mPresenter.mIsContactForEdit = false
            ChangeOptions.EDIT_CONTACT -> mPresenter.mIsContactForEdit = true
        }
    }

    private fun checkErrorsInFields(editable: Editable?){
        if (firstNameEditText.text!!.isEmpty()){
            inputLayoutFirstName.error = getString(R.string.valid_name)
            mErrorInFirstName          = true
        }

        else if (editable?.length!! > inputLayoutFirstName.counterMaxLength && firstNameEditText.hasFocus()){
            inputLayoutFirstName.error = getString(R.string.input_text_error_message) + inputLayoutFirstName.counterMaxLength
            mErrorInFirstName          = true
        }
        else{
            inputLayoutFirstName.error = null
            mErrorInFirstName          = false
        }

        if (editable!!.length > inputLayoutLastName.counterMaxLength && lastNameEditText.hasFocus()) {
            inputLayoutLastName.error = getString(R.string.input_text_error_message) + inputLayoutLastName.counterMaxLength
            mErrorInLastName          = true
        }
        else {
            inputLayoutLastName.error = null
            mErrorInLastName          = false
        }
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun areAllEmailsCorrect(): Boolean{
        if (emailFieldEditText.text.toString() !="" && emailFieldEditText.text.toString() !=" " ){

            if (!isValidEmail(emailFieldEditText.text)){
                emailTypeFieldLayout.error = getString(R.string.input_text_email_error_message)
                return false
        } else
            emailTypeFieldLayout.error = null

            if (emailLayout.childCount > 0){
                for (i in 1 until emailLayout.childCount){
                    val emailView: View = emailLayout.getChildAt(i)

                    if (!isValidEmail(emailView.fieldEditText.text.toString())){
                        emailView.typeFieldLayout.error = getString(R.string.input_text_email_error_message)
                        return false
                    }else
                        emailView.typeFieldLayout.error = null
                }
            }
        }
        emailTypeFieldLayout.error = null
        return true
    }

    private fun insertPhoneNumberRow(){
        val rowInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val phoneRow    = rowInflater.inflate(R.layout.activity_contact_row, null)

        phoneRow.fieldEditText.inputType = InputType.TYPE_CLASS_NUMBER
        phoneRow.removeChildView.setOnClickListener { phoneNumberLayout.removeView(it.parent as LinearLayout) }
        phoneNumberLayout.addView(phoneRow)
    }

    private fun insertEmailRow(){
       val rowInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
       val phoneRow    = rowInflater.inflate(R.layout.activity_contact_row, null)

        with(phoneRow){
        fieldEditText.addTextChangedListener(textWatcher)
        fieldEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        removeChildView.setOnClickListener {
            fieldEditText.setText(R.string.dummy_email)
            emailLayout.removeView(it.parent as LinearLayout) }
        }
        emailLayout.addView(phoneRow)
    }

    private fun getAllPhoneNumbers(): ArrayList<PhoneNumberModel>{
        val contactPhoneNumberModelList: ArrayList<PhoneNumberModel> = ArrayList()
        var phoneNumberModel = PhoneNumberModel(
                phoneNumber     = phoneNumberFieldEditText.text.toString(),
                phoneNumberType = mContactsData.getSpinnerTypeID(phoneNumberTypeSpinner.selectedItem.toString()))
        contactPhoneNumberModelList.add(phoneNumberModel)

        for(i in 1 until phoneNumberLayout.childCount){
            val phoneView: View  = phoneNumberLayout.getChildAt(i)

            if (!phoneView.fieldEditText.text.isNullOrEmpty()){
                phoneNumberModel = PhoneNumberModel(
                        phoneNumber     = phoneView.fieldEditText.text.toString(),
                        phoneNumberType = mContactsData.getSpinnerTypeID(phoneView.typeSpinner.selectedItem.toString()))
                contactPhoneNumberModelList.add(phoneNumberModel)
            }
        }
        return contactPhoneNumberModelList
    }

    private fun getAllEmails(): ArrayList<EmailModel>{
        val contactEmailModelList: ArrayList<EmailModel> = ArrayList()
        var emailModel = EmailModel(
                email     = emailFieldEditText.text.toString(),
                emailType = mContactsData.getSpinnerTypeID(emailTypeSpinner.selectedItem.toString()))
        contactEmailModelList.add(emailModel)

        for (i in 1 until emailLayout.childCount){
            val emailView: View = emailLayout.getChildAt(i)

            if (!emailView.fieldEditText.text.isNullOrEmpty()){
                emailModel = EmailModel(
                        email     = emailView.fieldEditText.text.toString(),
                        emailType = mContactsData.getSpinnerTypeID(emailView.typeSpinner.selectedItem.toString()))
                contactEmailModelList.add(emailModel)
            }
        }
        return contactEmailModelList
    }

    private fun saveContactToDB(){
        checkErrorsInFields(firstNameEditText.text)
        if (!mErrorInFirstName && !mErrorInLastName && areAllEmailsCorrect() ) {
            mPresenter.saveContactToDB(ContactModel(
                    firstName            = firstNameEditText.text.toString(),
                    lastName             = lastNameEditText.text.toString(),
                    phoneNumberModelList = getAllPhoneNumbers(),
                    emailModelList       = getAllEmails(),
                    dataBaseContact      = true))
        }
    }
    private fun setPhoneNumbersAndEmails(){
        val phoneNumberModelList: ArrayList<PhoneNumberModel> = mContact!!.phoneNumberModelList
        val emailModelList: ArrayList<EmailModel>             = mContact!!.emailModelList

        if(phoneNumberModelList.isNotEmpty()){
            phoneNumberTypeSpinner.setSelection(Math.toIntExact(phoneNumberModelList[0].phoneNumberType!!))
            phoneNumberFieldEditText.setText(phoneNumberModelList[0].phoneNumber)

            for(i in 1 until phoneNumberModelList.size){
                val phoneNumberModel: PhoneNumberModel = phoneNumberModelList[i]
                val phoneNumberRow: View               = layoutInflater.inflate(R.layout.activity_contact_row, null)

                with(phoneNumberRow) {
                    typeSpinner.setSelection(Math.toIntExact(phoneNumberModel.phoneNumberType!!))

                    fieldEditText.inputType = InputType.TYPE_CLASS_NUMBER
                    fieldEditText.setText(phoneNumberModel.phoneNumber)

                    removeChildView.tag = phoneNumberModel
                    removeChildView.setOnClickListener {

                        val phoneNumberModel = it.tag as PhoneNumberModel
                        phoneNumberModel.dbOperationType = Utils.DELETE
                        mPhoneNumbersList.add(phoneNumberModel)
                        phoneNumberLayout.removeView(it.parent as LinearLayout)
                    }
                }
                phoneNumberLayout.addView(phoneNumberRow)
            }
        }
        if (emailModelList.isNotEmpty()){
            emailTypeSpinner.setSelection(Math.toIntExact(emailModelList[0].emailType!!))
            emailFieldEditText.setText(emailModelList[0].email)

            for (i in 1 until emailModelList.size){
                val emailModel: EmailModel = emailModelList[i]
                val emailRow: View         = layoutInflater.inflate(R.layout.activity_contact_row, null)

                with(emailRow){
                    typeSpinner.setSelection(Math.toIntExact(emailModel.emailType!!))

                    fieldEditText.addTextChangedListener(textWatcher)
                    fieldEditText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    fieldEditText.setText(emailModel.email)

                    removeChildView.tag = emailModel
                    removeChildView.setOnClickListener {
                        fieldEditText.setText(R.string.dummy_email)
                        val emailModel = it.tag as EmailModel
                        emailModel.dbOperationType = Utils.DELETE
                        mEmailList.add(emailModel)
                        emailLayout.removeView(it.parent as LinearLayout)
                    }
                }
                emailLayout.addView(emailRow)
            }
        }
    }

    private fun updateContact(){
        if (firstNameEditText.text.toString() != mContact?.firstName && !firstNameEditText.text.isNullOrEmpty()){
            mContact?.firstName       = firstNameEditText.text.toString()
            mContact?.dbOperationType = Utils.UPDATE
        }

        if (lastNameEditText.text.toString() != mContact?.lastName){
            mContact?.lastName        = lastNameEditText.text.toString()
            mContact?.dbOperationType = Utils.UPDATE
        }

        if (phoneNumberFieldEditText.text.toString() != mContact?.phoneNumberModelList?.get(0)?.toString() && phoneNumberFieldEditText.text.toString().isNotEmpty()){
            val phoneNumberModel                = mContact?.phoneNumberModelList?.get(0)
            phoneNumberModel?.phoneNumber       = phoneNumberFieldEditText.text.toString()
            phoneNumberModel?.dbOperationType   = Utils.UPDATE
            mPhoneNumbersList.add(phoneNumberModel!!)
        }

        if (emailFieldEditText.text.toString() != mContact?.emailModelList?.get(0).toString() && emailFieldEditText.text.toString().isNotEmpty()){
            val emailModel              = mContact?.emailModelList?.get(0)
            emailModel?.email           = emailFieldEditText.text.toString()
            emailModel?.dbOperationType = Utils.UPDATE
            mEmailList.add(emailModel!!)
        }
        mContact?.phoneNumberModelList = defineDBOperationsForPhoneNumbers()
        mContact?.emailModelList       = defineDBOperationsForEmail()

        if (!firstNameEditText.text.toString().isNullOrEmpty() && !mErrorInFirstName && !mErrorInLastName && areAllEmailsCorrect())
         mPresenter.updateContactInDb(mContact!!)
    }

    private fun defineDBOperationsForPhoneNumbers(): ArrayList<PhoneNumberModel>{
        for (i in 1 until phoneNumberLayout.childCount){
            val phoneView = phoneNumberLayout.getChildAt(i)

            if (phoneView.removeChildView.tag == null){
                if (phoneView.fieldEditText.text.toString() != "")
                    mPhoneNumbersList.add(PhoneNumberModel(
                            phoneNumber     = phoneView.fieldEditText.text.toString(),
                            phoneNumberType =  mContactsData.getSpinnerTypeID(phoneView.typeSpinner.selectedItem.toString()),
                            dbOperationType =  Utils.INSERT))
            }
            else{
                val phoneNumberModel                 = phoneView.removeChildView.tag as PhoneNumberModel
                if (phoneNumberModel.phoneNumber    != phoneView.fieldEditText.text.toString() && phoneView.fieldEditText.text.toString() != "" ){
                    phoneNumberModel.phoneNumber     = phoneView.fieldEditText.text.toString()
                    phoneNumberModel.dbOperationType = Utils.UPDATE
                    mPhoneNumbersList.add(phoneNumberModel)
                }
            }
        }
        return mPhoneNumbersList
    }

    private fun defineDBOperationsForEmail(): ArrayList<EmailModel>{
        for (i in 1 until emailLayout.childCount){
            val emailView = emailLayout.getChildAt(i)

            if (emailView.removeChildView.tag == null){
                if (emailView.fieldEditText.text.toString() != "")
                    mEmailList.add(EmailModel(
                            email           =  emailView.fieldEditText.text.toString(),
                            emailType       =  mContactsData.getSpinnerTypeID(emailView.typeSpinner.selectedItem.toString()),
                            dbOperationType =  Utils.INSERT))
            }
            else{
                val emailModel                  = emailView.removeChildView.tag as EmailModel

                if (emailModel.email           != emailView.fieldEditText.text.toString() && emailView.fieldEditText.text.toString() != ""){
                    emailModel.email            = emailView.fieldEditText.text.toString()
                    emailModel.dbOperationType  = Utils.UPDATE
                    mEmailList.add(emailModel)
                }
            }
        }
        return mEmailList
    }

    override fun returnToMainActivity() { onBackPressed() }

    override fun setContactDetailsForEdit() {
        mContactsBinding.user = mContact
        contactImageView.setImageBitmap(mContact?.imageResource)
        saveButton.setOnClickListener {updateContact()}
        setPhoneNumbersAndEmails()
        supportActionBar?.setTitle(R.string.edit_contact)
    }

    override fun setContactDetailsForInsert() {
        saveButton.setOnClickListener {saveContactToDB()}
        supportActionBar?.setTitle(R.string.add_contact)
    }
}
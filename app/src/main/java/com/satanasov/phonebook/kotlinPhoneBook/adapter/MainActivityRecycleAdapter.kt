package com.satanasov.phonebook.kotlinPhoneBook.adapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.satanasov.phonebook.R
import com.satanasov.phonebook.databinding.PhoneBookRowBinding
import com.satanasov.phonebook.db.DataBaseQueries
import com.satanasov.phonebook.globalData.Utils
import com.satanasov.phonebook.kotlinPhoneBook.helpers.ContactsData
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel
import com.satanasov.phonebook.kotlinPhoneBook.model.EmailModel
import com.satanasov.phonebook.kotlinPhoneBook.model.PhoneNumberModel
import com.satanasov.phonebook.kotlinPhoneBook.view.ContactActivity
import kotlinx.android.synthetic.main.phone_book_row.view.*
import kotlinx.android.synthetic.main.phone_book_row_child.view.*

class MainActivityRecycleAdapter(context: Context) : ListAdapter<ContactModel,MyViewHolder>(ContactModelDiffUtil()) {
    private var mContext: Context? = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val phoneBookRowBinding = PhoneBookRowBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return MyViewHolder(phoneBookRowBinding, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position),mContext!!)
    }

    fun deleteItem(position: Int){
        val contactModel: ContactModel           = getItem(position)
        val dataBaseQueries: DataBaseQueries     = DataBaseQueries()
        val contactList: ArrayList<ContactModel> = ArrayList()
        contactList.addAll(currentList)
        contactList.remove(contactModel)
        submitList(contactList)
        dataBaseQueries.deleteContactById(mContext!!, contactModel.id!!)
    }

    fun goToContactsActivity(option: Utils.ChangeOptions, position: Int){
        val user: ContactModel = getItem(position)
        val intent             = Intent(mContext, ContactActivity::class.java)

        intent.putExtra(Utils.INTENT_EXTRA_OPTION, option)
        intent.putExtra(Utils.INTENT_USER_DETAILS, user)
        (mContext as Activity).startActivityForResult(intent, Utils.GO_TO_CONTACT_ACTIVITY_EDIT)
    }
}

class MyViewHolder(binding: PhoneBookRowBinding, adapter: MainActivityRecycleAdapter) : RecyclerView.ViewHolder(binding.root) {
    private var mAdapter:MainActivityRecycleAdapter  = adapter
    private val mBinding: PhoneBookRowBinding        = binding

    fun bind(contactModel: ContactModel?, context: Context) {
        val contactsData: ContactsData = ContactsData(context)
        mBinding.user = contactModel

        with(itemView) {
            expandableAreaPhoneNumbers.removeAllViews()
            expandableAreaEmails.removeAllViews()

            if (hiddenLayout.visibility == View.VISIBLE) {
                hiddenLayout.visibility     = View.GONE
                expandLayoutButton.rotation = Utils.ROTATE_TO_0_DEGREES
            }

            if (!contactModel!!.dataBaseContact) {
                deleteContact.visibility = View.GONE
                editContact.visibility   = View.GONE
                imageBorder.setBackgroundResource(0)
            } else {
                deleteContact.visibility = View.VISIBLE
                editContact.visibility   = View.VISIBLE
                imageBorder.setBackgroundResource(R.drawable.circular_border)
            }
            imageViewPhoneBookRow.setImageBitmap(contactModel.imageResource)

            for (phoneNumberModel: PhoneNumberModel in contactModel.phoneNumberModelList) {
                val rowView: View = LayoutInflater.from(context).inflate(R.layout.phone_book_row_child, expandableAreaPhoneNumbers, false)

                rowView.textFieldTextView.setText(phoneNumberModel.phoneNumber)
                rowView.typeFieldTextView.setText(contactsData.getSpinnerTypeText(phoneNumberModel.phoneNumberType!!))
                expandableAreaPhoneNumbers.addView(rowView)
            }

            for (emailModel: EmailModel in contactModel.emailModelList) {
                val rowView: View = LayoutInflater.from(context).inflate(R.layout.phone_book_row_child, expandableAreaEmails, false)

                rowView.textFieldTextView.setText(emailModel.email)
                rowView.typeFieldTextView.setText(contactsData.getSpinnerTypeText(emailModel.emailType!!))
                expandableAreaEmails.addView(rowView)
            }

            expandLayoutButton.setOnClickListener {
                if (hiddenLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(parentConstraintLayout)
                    hiddenLayout.visibility     = View.VISIBLE
                    expandLayoutButton.rotation = Utils.ROTATE_TO_180_DEGREES
                } else {
                    hiddenLayout.visibility     = View.GONE
                    expandLayoutButton.rotation = Utils.ROTATE_TO_0_DEGREES
                }
            }

            editContact.setOnClickListener {
                mAdapter.goToContactsActivity(Utils.ChangeOptions.EDIT_CONTACT, adapterPosition)
            }

            deleteContact.setOnClickListener {
                hiddenLayout.visibility     = View.GONE
                expandLayoutButton.rotation = Utils.ROTATE_TO_0_DEGREES
                mAdapter.deleteItem(adapterPosition)
            }
        }
        mBinding.executePendingBindings()
    }
}

class ContactModelDiffUtil: DiffUtil.ItemCallback<ContactModel>(){
    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.id == newItem.id }

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem == newItem   }

}



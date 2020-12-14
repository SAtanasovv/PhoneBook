package com.satanasov.phonebook.kotlinPhoneBook.adapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
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

class MainActivityRecycleAdapter(contactModelList: ArrayList<ContactModel>, context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    private var mContactModelList: ArrayList<ContactModel>?  = null
    private var mContext: Context?                           = null

    init {
        mContext          = context
        mContactModelList = contactModelList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val phoneBookRowBinding = PhoneBookRowBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return MyViewHolder(phoneBookRowBinding, mContactModelList!!, this)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mContactModelList!![position],mContext!!)
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.expandableAreaPhoneNumbers.removeAllViews()
        holder.itemView.expandableAreaEmails.removeAllViews()

        if (holder.adapterPosition >= 0){
            val contactModel:ContactModel = mContactModelList!![holder.adapterPosition]

            if (contactModel.expanded){
                contactModel.expanded = false
                holder.itemView.hiddenLayout.visibility     = View.GONE
                holder.itemView.expandLayoutButton.rotation = Utils.ROTATE_TO_0_DEGREES
            }
        }
    }

    override fun getItemCount(): Int {
        return mContactModelList!!.size
    }

    fun deleteItem(position: Int){
        val contactModel: ContactModel       = mContactModelList!![position]
        val dataBaseQueries: DataBaseQueries = DataBaseQueries()
        contactModel.expanded = false
        mContactModelList!!.removeAt(position)
        dataBaseQueries.deleteContactById(mContext!!, contactModel.id!!)
        notifyItemRemoved(position)
    }

    fun updateAdapterData(updatedList: ArrayList<ContactModel>){
        mContactModelList = ArrayList()
        mContactModelList = updatedList
    }

    fun goToContactsActivity(option: Utils.ChangeOptions, position: Int){
        val user: ContactModel = mContactModelList!![position]

        val intent    = Intent(mContext, ContactActivity::class.java)
        user.expanded = false
        intent.putExtra(Utils.INTENT_EXTRA_OPTION, option)
        intent.putExtra(Utils.INTENT_USER_DETAILS, user)
        (mContext as Activity).startActivityForResult(intent, Utils.GO_TO_CONTACT_ACTIVITY_EDIT)
    }
}

class MyViewHolder(binding: PhoneBookRowBinding, contactModelArrayList: ArrayList<ContactModel>, adapter: MainActivityRecycleAdapter) : RecyclerView.ViewHolder(binding.root) {
    private var mAdapter:MainActivityRecycleAdapter         = adapter

    private val mContactModelList: ArrayList<ContactModel>  = contactModelArrayList
    private val mBinding: PhoneBookRowBinding               = binding

    fun bind(contactModel: ContactModel?, context: Context) {
        val contactsData: ContactsData = ContactsData(context)
        mBinding.user = contactModel

        if (!contactModel!!.dataBaseContact) {
            itemView.deleteContact.visibility = View.GONE
            itemView.editContact.visibility   = View.GONE
            itemView.imageBorder.setBackgroundResource(0)
        } else {
            itemView.deleteContact.visibility = View.VISIBLE
            itemView.editContact.visibility   = View.VISIBLE
            itemView.imageBorder.setBackgroundResource(R.drawable.circular_border)
        }
        itemView.imageViewPhoneBookRow.setImageBitmap(contactModel.imageResource)

        for (phoneNumberModel: PhoneNumberModel in contactModel.phoneNumberModelList){
            val rowView: View = LayoutInflater.from(context).inflate(R.layout.phone_book_row_child,itemView.expandableAreaPhoneNumbers,false)

            rowView.textFieldTextView.setText(phoneNumberModel.phoneNumber)
            rowView.typeFieldTextView.setText(contactsData.getSpinnerTypeText(phoneNumberModel.phoneNumberType!!))
            itemView.expandableAreaPhoneNumbers.addView(rowView)
        }

        for (emailModel: EmailModel in contactModel.emailModelList){
            val rowView: View = LayoutInflater.from(context).inflate(R.layout.phone_book_row_child,itemView.expandableAreaEmails,false)

            rowView.textFieldTextView.setText(emailModel.email)
            rowView.typeFieldTextView.setText(contactsData.getSpinnerTypeText(emailModel.emailType!!))
            itemView.expandableAreaEmails.addView(rowView)
        }
        mBinding.executePendingBindings()
    }

    init {
        itemView.expandLayoutButton.setOnClickListener {
            val contact      = mContactModelList[adapterPosition]
            contact.expanded = !contact.expanded

            if (contact.expanded) {
                TransitionManager.beginDelayedTransition(itemView.parentConstraintLayout)
                itemView.hiddenLayout.visibility       = View.VISIBLE
                itemView.expandLayoutButton.rotation   = Utils.ROTATE_TO_180_DEGREES
            } else {
                itemView.hiddenLayout.visibility       = View.GONE
                itemView.expandLayoutButton.rotation   = Utils.ROTATE_TO_0_DEGREES
            }
        }

        itemView.editContact.setOnClickListener { mAdapter.goToContactsActivity(Utils.ChangeOptions.EDIT_CONTACT, adapterPosition) }

        itemView.deleteContact.setOnClickListener {
            itemView.hiddenLayout.visibility     = View.GONE
            itemView.expandLayoutButton.rotation = Utils.ROTATE_TO_0_DEGREES
            mAdapter.deleteItem(adapterPosition)
        }
    }
}

class ContactModelDiffUtil: DiffUtil.ItemCallback<ContactModel>(){
    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
       return oldItem.id == newItem.id }

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem == newItem   }

}




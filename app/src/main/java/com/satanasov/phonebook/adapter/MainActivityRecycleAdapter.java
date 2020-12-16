package com.satanasov.phonebook.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.satanasov.phonebook.helpers.ContactsData;
import com.satanasov.phonebook.databinding.PhoneBookRowBinding;
import com.satanasov.phonebook.db.DataBaseQueries;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.kotlinPhoneBook.view.ContactActivity;
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.kotlinPhoneBook.model.EmailModel;
import com.satanasov.phonebook.kotlinPhoneBook.model.PhoneNumberModel;
import java.util.ArrayList;

public class MainActivityRecycleAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<ContactModel> mContactNumberList;
    private Context                 mContext;

    public MainActivityRecycleAdapter(ArrayList<ContactModel> dummyUserList, Context context){
        this.mContactNumberList = dummyUserList;
        this.mContext           = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater           = LayoutInflater.from(mContext);
        PhoneBookRowBinding phoneBookRowBinding = PhoneBookRowBinding.inflate(layoutInflater,parent,false);
        return  new MyViewHolder(phoneBookRowBinding, mContactNumberList,this);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ContactModel user          = mContactNumberList.get(position);
        ContactsData contactsData  = new ContactsData(mContext);

        if (!user.getDataBaseContact()){
            holder.mDeleteButton.setVisibility(View.GONE);
            holder.mEditButton.setVisibility(View.GONE);
            holder.mContactImageBorder.setBackgroundResource(0);
        }
        else{
            holder.mDeleteButton.setVisibility(View.VISIBLE);
            holder.mEditButton.setVisibility(View.VISIBLE);
            holder.mContactImageBorder.setBackgroundResource(R.drawable.circular_border);
        }

        holder.mContactImage.setImageBitmap(user.getImageResource());

        for (PhoneNumberModel phoneNumber : user.getPhoneNumberModelList()){

            View rowView      = LayoutInflater.from(mContext).inflate(R.layout.phone_book_row_child, holder.mExpandablePhoneNumberLayout, false);
            TextView text     = rowView.findViewById(R.id.textFieldTextView);
            TextView textType = rowView.findViewById(R.id.typeFieldTextView);

            text.setText(phoneNumber.getPhoneNumber());
            textType.setText(contactsData.getSpinnerTypeText(phoneNumber.getPhoneNumberType()));
            holder.mExpandablePhoneNumberLayout.addView(rowView);
        }
        for (EmailModel emailModel : user.getEmailModelList()){

            View rowView      = LayoutInflater.from(mContext).inflate(R.layout.phone_book_row_child, holder.mExpandableEmailLayout, false);
            TextView text     = rowView.findViewById(R.id.textFieldTextView);
            TextView textType = rowView.findViewById(R.id.typeFieldTextView);

            text.setText(emailModel.getEmail());
            textType.setText(contactsData.getSpinnerTypeText(emailModel.getEmailType()));
            holder.mExpandableEmailLayout.addView(rowView);
        }
        holder.bind(user);
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
        holder.mExpandablePhoneNumberLayout.removeAllViews();
        holder.mExpandableEmailLayout.removeAllViews();

        if (holder.getAdapterPosition() >= 0) {
            ContactModel contact = mContactNumberList.get(holder.getAdapterPosition());

            if (contact.getExpanded()) {
                contact.setExpanded(false);
                holder.mHiddenLayout.setVisibility(View.GONE);
                holder.mExpandButton.setRotation(Utils.ROTATE_TO_0_DEGREES);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mContactNumberList.size();
    }

    public void deleteItem(int position){
        ContactModel    contactModel    = mContactNumberList.get(position);
        DataBaseQueries dataBaseQueries = new DataBaseQueries();

        contactModel.setExpanded(false);
        mContactNumberList.remove(position);
        dataBaseQueries.deleteContactById(mContext,contactModel.getId());
        notifyItemRemoved(position);
    }

    public void updateAdapterData(ArrayList<ContactModel> updatedList){

        this.mContactNumberList = new ArrayList<>();
        this.mContactNumberList.addAll(updatedList);
    }

    public void goToContactsActivity(Utils.ChangeOptions option, int position){
        ContactModel user  = mContactNumberList.get(position);
        Intent intent      = new Intent(mContext, ContactActivity.class);
        user.setExpanded(false);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION, option);
        intent.putExtra(Utils.INTENT_USER_DETAILS, user);
        ((Activity)mContext).startActivityForResult(intent,Utils.GO_TO_CONTACT_ACTIVITY_EDIT);
    }
}

 class MyViewHolder extends RecyclerView.ViewHolder {

    public  MainActivityRecycleAdapter mAdapter;
    public  LinearLayout               mContactView;
    public  ConstraintLayout           mHiddenLayout;
    public  LinearLayout               mExpandablePhoneNumberLayout;
    public  LinearLayout               mExpandableEmailLayout;
    public  RelativeLayout             mContactImageBorder;

    public  ImageView                  mContactImage;
    public  CardView                   mContactImageFrame;
    public  ImageButton                mExpandButton;
    public  Button                     mEditButton;
    public  Button                     mDeleteButton;
    private ArrayList<ContactModel>    mContactModelList;

    private PhoneBookRowBinding        mBinding;

    public MyViewHolder(PhoneBookRowBinding binding, final ArrayList<ContactModel> contactModelArrayList,MainActivityRecycleAdapter adapter) {
        super(binding.getRoot());

        this.mBinding           = binding;

        this.mContactModelList  = contactModelArrayList;
        this.mAdapter           = adapter;

        mContactView                 = binding.parentConstraintLayout;
        mExpandablePhoneNumberLayout = binding.expandableAreaPhoneNumbers;
        mExpandableEmailLayout       = binding.expandableAreaEmails;
        mHiddenLayout                = binding.hiddenLayout;

        mContactImage       = binding.imageViewPhoneBookRow;
        mContactImageFrame  = binding.cardViewPhoneBookRow;
        mContactImageBorder = binding.imageBorder;
        mExpandButton       = binding.expandLayoutButton;
        mEditButton         = binding.editContact;
        mDeleteButton       = binding.deleteContact;

        mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactModel contact = MyViewHolder.this.mContactModelList.get(getAdapterPosition());
                contact.setExpanded(!contact.getExpanded());

                if (contact.getExpanded()){
                    TransitionManager.beginDelayedTransition(mContactView);
                    mHiddenLayout.setVisibility(View.VISIBLE);
                    mExpandButton.setRotation(Utils.ROTATE_TO_180_DEGREES);
                }
                else {
                    mHiddenLayout.setVisibility(View.GONE);
                    mExpandButton.setRotation(Utils.ROTATE_TO_0_DEGREES);
                }
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.goToContactsActivity(Utils.ChangeOptions.EDIT_CONTACT,getAdapterPosition());
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHiddenLayout.setVisibility(View.GONE);
                mExpandButton.setRotation(Utils.ROTATE_TO_0_DEGREES);
                mAdapter.deleteItem(getAdapterPosition());
            }
        });
    }

    public void bind(ContactModel user){
        mBinding.setUser(user);
        mBinding.executePendingBindings();
    }
}

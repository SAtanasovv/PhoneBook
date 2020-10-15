package com.satanasov.phonebook.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import com.satanasov.phonebook.Helpers.ContactsData;
import com.satanasov.phonebook.databinding.PhoneBookRowBinding;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.satanasov.phonebook.view.ContactsActivity;
import java.util.ArrayList;

public class MainActivityRecycleAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<ContactModel>  mDummyUserList;
    private Context                  mContext;

    public MainActivityRecycleAdapter(ArrayList<ContactModel> mDummyUserList, Context context){
        this.mDummyUserList  = mDummyUserList;
        this.mContext        = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        PhoneBookRowBinding phoneBookRowBinding = PhoneBookRowBinding.inflate(layoutInflater,parent,false);
        return  new MyViewHolder(phoneBookRowBinding,mContext,mDummyUserList);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ContactModel user          = mDummyUserList.get(position);
        ContactsData mContactsData = new ContactsData(mContext);

        if (!user.isDataBaseContact())
            holder.mViewButton.setVisibility(View.GONE);
        else
            holder.mViewButton.setVisibility(View.VISIBLE);

        holder.mContactImage.setImageBitmap(user.getImageId());

        for (PhoneNumberModel phoneNumber : user.getPhoneNumberModelList()){
            View rowView      = LayoutInflater.from(mContext).inflate(R.layout.phone_book_row_child, holder.mExpandableLayout, false);
            TextView text     = rowView.findViewById(R.id.text_field_text_view);
            TextView textType = rowView.findViewById(R.id.type_field_text_view);

            text.setText(phoneNumber.getPhoneNumber());
            textType.setText( mContactsData.getPhoneNumberTypeText(phoneNumber.getPhoneNumberType()));
            holder.mExpandableLayout.addView(rowView);
        }
        holder.bind(user);
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);

        holder.mExpandableLayout.removeAllViews();
        ContactModel contact = mDummyUserList.get(holder.getAdapterPosition());

        if (contact.isExpanded()){
        contact.setExpanded(false);
        holder.mHiddenLayout.setVisibility(View.GONE);
        holder.mExpandButton.setRotation(Utils.ROTATE_TO_0_DEGREES);
       }
    }

    @Override
    public int getItemCount() {
        return mDummyUserList.size();
    }
}

 class MyViewHolder extends RecyclerView.ViewHolder {

    public  LinearLayout             mContactView;
    public  ConstraintLayout         mHiddenLayout;
    public  LinearLayout             mExpandableLayout;

    public  ImageView                mContactImage;
    public  ImageButton              mExpandButton;
    public  Button                   mEditButton;
    public  Button                   mViewButton;
    private ArrayList<ContactModel>  mDummyUserList;

    private Context                  mContext;
    private PhoneBookRowBinding      mBinding;

    public MyViewHolder(PhoneBookRowBinding binding, Context context, final ArrayList<ContactModel> mDummyUserList) {
        super(binding.getRoot());

        this.mBinding       = binding;
        this.mContext       = context;
        this.mDummyUserList = mDummyUserList;

        mContactView      = binding.parentConstraintLayout;
        mExpandableLayout = binding.expandableAreaPhoneNumbers;
        mHiddenLayout     = binding.hiddenLayout;

        mContactImage     = binding.imageViewPhoneBookRowId;
        mExpandButton     = binding.contactSettingsButtonPhoneBookRowId;
        mEditButton       = binding.editContact;
        mViewButton       = binding.viewContact;

        mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactModel contact = mDummyUserList.get(getAdapterPosition());
                contact.setExpanded(!contact.isExpanded());

                if (contact.isExpanded()){
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
                goToContactsActivity(Utils.ChangeOptions.EDIT_CONTACT,getAdapterPosition());
            }
        });

        mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContactsActivity(Utils.ChangeOptions.VIEW_CONTACT,getAdapterPosition());
            }
        });
    }

    private void goToContactsActivity(Utils.ChangeOptions option, int position){
        ContactModel user  = mDummyUserList.get(position);
        Intent intent      = new Intent(mContext, ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION, option);
        intent.putExtra(Utils.INTENT_USER_DETAILS, user);
        mContext.startActivity(intent);
    }

    public void bind(ContactModel user){
        mBinding.setUser(user);
        mBinding.executePendingBindings();
    }
}

package com.satanasov.phonebook.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.card.MaterialCardView;
import com.satanasov.phonebook.databinding.PhoneBookRowBinding;
import com.satanasov.phonebook.databinding.PhoneBookRowChildBinding;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.model.PhoneNumberModel;
import com.satanasov.phonebook.view.ContactsActivity;
import java.util.ArrayList;

public class MainActivityRecycleAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<ContactModel>         mDummyUserList;
    private Context                         mContext;
    private static final int                      mWORK_NUMBER = 1;
    private static final int                      mMAIN_NUMBER = 2;
    private static final int                      mHOME_NUMBER = 3;
    private static final int                      mMOBILE_NUMBER = 4;

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

    /*
    https://stackoverflow.com/questions/49178019/dynamically-add-table-and-rows-in-cardviews-contained-in-recyclerview
    https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-type
    https://stackoverflow.com/questions/27128425/add-multiple-custom-views-to-layout-programmatically
    https://stackoverflow.com/questions/34552070/dynamically-adding-views-in-a-recyclerview-only-to-current-item/42482524#42482524

    */

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        ContactModel user  = mDummyUserList.get(position);
        holder.mContactImage.setImageBitmap(user.getImageId());

        for (PhoneNumberModel phoneNumber : user.getPhoneNumberModelList()){
            View rowView      = LayoutInflater.from(mContext).inflate(R.layout.phone_book_row_child, holder.mExpandableLayout, false);
            TextView text     = rowView.findViewById(R.id.text_field_text_view);
            TextView textType = rowView.findViewById(R.id.type_field_text_view);
            text.setText(phoneNumber.getPhoneNumber());
            switch (phoneNumber.getPhoneNumberType().intValue()){
                case mHOME_NUMBER:
                    textType.setText(R.string.type_home);
                break;

                case mWORK_NUMBER:
                    textType.setText(R.string.type_work);
                break;

                case mMAIN_NUMBER:
                    textType.setText(R.string.type_main);
                break;

                case mMOBILE_NUMBER:
                    textType.setText(R.string.type_mobile);
                break;

            }
            holder.setIsRecyclable(false);
            holder.mExpandableLayout.addView(rowView);


        }
        holder.bind(user);

    }

    @Override
    public int getItemCount() {
        return mDummyUserList.size();
    }

}

 class MyViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout          mContactCardView;
    public  LinearLayout             mExpandableLayout;
    public  ImageView                mContactImage;
    public  ImageButton              mExpandButton;
    private ArrayList<ContactModel>  mDummyUserList;
    private Context                  mContext;
    private PhoneBookRowBinding      mBinding;

    public MyViewHolder(PhoneBookRowBinding binding, Context context, final ArrayList<ContactModel> mDummyUserList) {
        super(binding.getRoot());

        this.mBinding       = binding;
        this.mContext       = context;
        this.mDummyUserList = mDummyUserList;

        mContactCardView    = binding.parentCardView;
        mExpandButton       = binding.contactSettingsButtonPhoneBookRowId;
        mExpandableLayout   = binding.expandableArea;
        mContactImage       = binding.imageViewPhoneBookRowId;

        mExpandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactModel contact = mDummyUserList.get(getAdapterPosition());
                contact.setExpanded(!contact.isExpanded());

                if (contact.isExpanded()){
                    TransitionManager.beginDelayedTransition(mContactCardView);
                    mExpandableLayout.setVisibility(View.VISIBLE);
                    mExpandButton.setRotation(180f);
                }
                else
                {
                    TransitionManager.beginDelayedTransition(mContactCardView);
                    mExpandableLayout.setVisibility(View.GONE);
                    mExpandButton.setRotation(0f);
                }
            }
        });
    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.edit_option:
                        goToContactsActivity(Utils.ChangeOptions.EDIT_CONTACT,getAdapterPosition());
                    return true;

                    case R.id.view_option:
                        goToContactsActivity(Utils.ChangeOptions.VIEW_CONTACT,getAdapterPosition());
                    return true;

                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.options_menu, popup.getMenu());
        popup.show();
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

package com.satanasov.phonebook.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.satanasov.phonebook.databinding.PhoneBookRowBinding;
import com.satanasov.phonebook.databinding.PhoneBookRowChildBinding;
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
        ContactModel user  = mDummyUserList.get(position);
        holder.mContactImage.setImageBitmap(user.getImageId());
        holder.bind(user);
        holder.mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactModel contact = mDummyUserList.get(position);
                boolean expanded = contact.isExpanded();
                contact.setExpanded(!expanded);
                holder.mExpandableLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDummyUserList.size();
    }

}

 class MyViewHolder extends RecyclerView.ViewHolder {


    public  LinearLayout            mExpandableLayout;
    public  ImageView               mContactImage;
    public  ImageButton             mOptions;
    private ArrayList<ContactModel> mDummyUserList;
    private Context                 mContext;
    private PhoneBookRowBinding     mBinding;

    public MyViewHolder(PhoneBookRowBinding binding, Context context, final ArrayList<ContactModel> mDummyUserList) {
        super(binding.getRoot());

        this.mBinding       = binding;
        this.mContext       = context;
        this.mDummyUserList = mDummyUserList;

        mOptions            = binding.contactSettingsButtonPhoneBookRowId;
        mExpandableLayout   = binding.expandableArea;
        mContactImage       = binding.imageViewPhoneBookRowId;

//        mOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
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

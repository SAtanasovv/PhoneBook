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
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.satanasov.phonebook.databinding.PhoneBookRowBinding;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.model.UserModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.view.ContactsActivity;
import java.util.ArrayList;

public class MainActivityRecycleAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<UserModel>     mDummyUserList;
    private Context             mContext;

    public MainActivityRecycleAdapter(ArrayList<UserModel> mDummyUserList, Context context){
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel user  = mDummyUserList.get(position);
        holder.mContactImage.setImageBitmap(user.getImageId());
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return mDummyUserList.size();
    }
}

 class MyViewHolder extends RecyclerView.ViewHolder {

    public ImageView            mContactImage;
    public ImageButton          mOptions;
    private ArrayList<UserModel>     mDummyUserList;
    private Context             mContext;
    private PhoneBookRowBinding mBinding;

    public MyViewHolder(PhoneBookRowBinding binding,Context context,ArrayList<UserModel> mDummyUserList) {
        super(binding.getRoot());
        this.mBinding       = binding;
        this.mContext       = context;
        this.mDummyUserList = mDummyUserList;

        mOptions            = binding.contactSettingsButtonPhoneBookRowId;
        mContactImage       = binding.imageViewPhoneBookRowId;

        mOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(view);
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
                        goToContactsActivity(Utils.ChangeOptions.EDIT_CONTACT, getAdapterPosition());
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
        UserModel user     = mDummyUserList.get(position);
        Intent intent = new Intent(mContext, ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION, option);
        intent.putExtra(Utils.INTENT_USER_DETAILS, user);
        mContext.startActivity(intent);
    }

    public void bind(UserModel user){
        mBinding.setUser(user);
        mBinding.executePendingBindings();
    }
}

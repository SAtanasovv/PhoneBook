package com.satanasov.phonebook.Adapter;
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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.satanasov.phonebook.GlobalData.Utils;
import com.satanasov.phonebook.Model.User;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.View.ContactsActivity;
import java.util.ArrayList;

public class MainActivityRecycleAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<User>     mDummyUserList;
    private Context             mContext;

    public MainActivityRecycleAdapter(ArrayList<User> mDummyUserList,Context context) {
        this.mDummyUserList =   mDummyUserList;
        this.mContext       =   context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_book_row,parent,false);
        return  new MyViewHolder(view,mContext,mDummyUserList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user  = mDummyUserList.get(position);
        holder.mFirstName.setText(user.getFirstName());
        holder.mLastName.setText(user.getLastName());
        holder.mProfilePicture.setImageResource(user.getImageId());
    }

    @Override
    public int getItemCount() {
        return mDummyUserList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView             mFirstName;
    public TextView             mLastName;
    public ImageButton          mOptions;
    public ImageView            mProfilePicture;

    private ArrayList<User>     mDummyUserList;
    private Context             mContext;




    public MyViewHolder(@NonNull View view,Context context,ArrayList<User> mDummyUserList) {
        super(view);
        this.mContext       = context;
        this.mDummyUserList = mDummyUserList;

        mFirstName          = view.findViewById(R.id.first_name_text_view_phone_book_row_id);
        mLastName           = view.findViewById(R.id.last_name_text_view_phone_book_row_id);
        mOptions            = view.findViewById(R.id.contact_settings_button_phone_book_row_id);
        mProfilePicture     = view.findViewById(R.id.image_view_phone_book_row_id);

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

    private void goToContactsActivity(Utils.ChangeOptions option, int position) {
        User user     = mDummyUserList.get(position);
        Intent intent = new Intent(mContext, ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION, option);
        intent.putExtra(Utils.INTENT_USER_DETAILS, user);
        mContext.startActivity(intent);
    }
}

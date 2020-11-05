package com.satanasov.phonebook.view;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.satanasov.phonebook.adapter.MainActivityRecycleAdapter;
import com.satanasov.phonebook.databinding.ActivityMainBinding;
import com.satanasov.phonebook.model.ContactModel;
import com.satanasov.phonebook.R;
import com.satanasov.phonebook.globalData.Utils.ChangeOptions;
import com.satanasov.phonebook.globalData.Utils;
import com.satanasov.phonebook.presenter.MainActivityPresenter;
import com.satanasov.phonebook.presenter.MainActivityView;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements MainActivityView {

    private RecyclerView                mRecyclerView;
    private FloatingActionButton        mFloatingButton;
    private TextView                    mNoContactsTextView;

    private MainActivityRecycleAdapter  mAdapter;
    private ActivityMainBinding         mBinding;
    private MainActivityPresenter       mPresenter;

    public static final int             mPERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding    = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mPresenter  = new MainActivityPresenter(this);
        getPermissionToReadContactsFromInternalStorage();
        init();
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
             mPresenter.subscribe();

        else {
            mNoContactsTextView.setVisibility(View.VISIBLE);
            mFloatingButton.setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        mPresenter.unSubscribe();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    private void init(){
        mNoContactsTextView = findViewById(R.id.no_contacts_available);
        mFloatingButton     = findViewById(R.id.add_floating_button_main_activity_id);

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContactsActivity(ChangeOptions.ADD_CONTACT);
            }
        });

        Toolbar toolbar  = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initAdapter(){
        ArrayList<ContactModel> mergedList   = new ArrayList<>();

        mAdapter      = new MainActivityRecycleAdapter(mergedList,(Context) this);
        mRecyclerView = mBinding.recyclerViewMainActivityId;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void goToContactsActivity(ChangeOptions option){
        Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
        intent.putExtra(Utils.INTENT_EXTRA_OPTION,option);
        startActivity(intent);
    }

    public void getPermissionToReadContactsFromInternalStorage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setMessage(R.string.permission_message);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[] {android.Manifest.permission.READ_CONTACTS}, mPERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                });
                builder.show();
            }
            else
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, mPERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == mPERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mNoContactsTextView.setVisibility(View.INVISIBLE);
                mFloatingButton.setEnabled(true);
                mPresenter.subscribe();
            }
            else
                Toast.makeText(this, R.string.permission_unsuccessful, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setContactListInRecyclerView(ArrayList<ContactModel> contactModelList) {
        mAdapter.updateAdapterData(contactModelList);
        mAdapter.notifyDataSetChanged();
    }
}
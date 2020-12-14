package com.satanasov.phonebook.kotlinPhoneBook.view
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.satanasov.phonebook.R
import com.satanasov.phonebook.kotlinPhoneBook.adapter.MainActivityRecycleAdapter
import com.satanasov.phonebook.databinding.ActivityMainBinding
import com.satanasov.phonebook.globalData.Utils
import com.satanasov.phonebook.globalData.Utils.ChangeOptions
import com.satanasov.phonebook.kotlinPhoneBook.model.ContactModel
import com.satanasov.phonebook.kotlinPhoneBook.presenter.MainActivityPresenter
import com.satanasov.phonebook.kotlinPhoneBook.presenter.MainActivityView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainActivityView  {
    private lateinit var mAdapter:    MainActivityRecycleAdapter //JavaClass
    private lateinit var mBinding:    ActivityMainBinding
    private lateinit var mPresenter:  MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding   = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mPresenter = MainActivityPresenter(this)

        getPermissionToReadContactsFromInternalStorage()
        init()
    }
   private fun init() {
        with(mBinding){
            bindingMainActivity     = MainActivity()
            setSupportActionBar(mainToolbar as Toolbar)
        }
       val mergedList  = ArrayList<ContactModel>()

       mAdapter        = MainActivityRecycleAdapter(mergedList, this)
       addFloatingButtonMainActivity.setOnClickListener { goToContactsActivity() }

       recyclerViewMainActivity.setHasFixedSize(true)
       recyclerViewMainActivity.layoutManager = LinearLayoutManager(this)
       recyclerViewMainActivity.adapter       = mAdapter
   }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            mPresenter.subscribe()
        else {
            noContactsAvailable.visibility           = View.VISIBLE
            addFloatingButtonMainActivity.isEnabled  = false
        }
    }

    override fun onPause() {
        mPresenter.unSubscribe()
        super.onPause()
    }

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
    }

    private fun goToContactsActivity() {
        startActivity(Intent(this, ContactActivity::class.java).putExtra(Utils.INTENT_EXTRA_OPTION, ChangeOptions.ADD_CONTACT))
    }

    private fun getPermissionToReadContactsFromInternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.permission_title)
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setMessage(R.string.permission_message)
                builder.setOnDismissListener { requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), mPERMISSIONS_REQUEST_READ_CONTACTS) }
                builder.show()
            }
            else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), mPERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == mPERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                noContactsAvailable.visibility             = View.INVISIBLE
                addFloatingButtonMainActivity.isEnabled    = true
                mPresenter.subscribe()
            }
            else
                Toast.makeText(this, R.string.permission_unsuccessful, Toast.LENGTH_LONG).show()
        }
    }

    override fun setContactListInRecyclerView(contactModel: ArrayList<ContactModel>) {
        mAdapter.updateAdapterData(contactModel)
        mAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val mPERMISSIONS_REQUEST_READ_CONTACTS = 1
    }
}
package com.cc.phonecontacts.ui.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.cc.phonecontacts.R
import com.cc.phonecontacts.adapter.ContactAdapter
import com.cc.phonecontacts.databinding.ActivityContactsBinding
import com.cc.phonecontacts.util.ReadContactPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ContactsActivity : AppCompatActivity() {
    private val TAG = "ContactsActivity"

    @Inject
    lateinit var readContactPermission: ReadContactPermission
    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var binding: ActivityContactsBinding
    private var contactAdapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts)
        binding.lifecycleOwner = this
        setUpRecycler()
        setUpListeners()

    }

    private fun setUpListeners() {
        binding.requestPermission.setOnClickListener { requestPermissions() }
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactAdapter?.filterContacts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }


    private fun setUpRecycler() {
        contactAdapter = ContactAdapter(viewModel.contactList, this) {
            startActivity(
                Intent(this@ContactsActivity, ContactDetailActivity::class.java).putExtra(
                    "contact" , it
                )
            )
        }
       binding.contactAdapter = contactAdapter
        binding.hintText = "Search among ${viewModel.contactList.size} contact(s)"
    }



    private fun requestPermissions() {
        if (readContactPermission(this, Manifest.permission.READ_CONTACTS)) {
            // ReadContactPermission are already granted, do your stuff
            binding.requestPermission.visibility = View.GONE
            viewModel.getContacts(){
               contactAdapter?.notifyItemInserted(viewModel.contactList.size.minus(1))
                binding.hintText = "Search among ${viewModel.contactList.size} contact(s)"
            }
        } else {
            binding.requestPermission.visibility = View.VISIBLE
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                    binding.requestPermission.visibility = View.GONE
                  //  collectData()
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()
                    binding.requestPermission.visibility = View.VISIBLE
                }
                return
            }
        }
    }

    companion object {
        val REQUEST_CODE = 101
    }


    override fun onResume() {
        super.onResume()
        requestPermissions()
    }


}
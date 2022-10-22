package com.cc.phonecontacts.util

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cc.phonecontacts.ui.contacts.ContactsActivity
import com.cc.phonecontacts.ui.contacts.ContactsActivity.Companion.REQUEST_CODE
import javax.inject.Inject

class ReadContactPermission @Inject constructor() {

     private fun isPermissionsAllowed(activity: ContactsActivity ,permission:String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    operator fun invoke(activity:  ContactsActivity,permission:String): Boolean {
        if (!isPermissionsAllowed(activity, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                showPermissionDeniedDialog(activity)
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_CODE
                )
            }
            return false
        }
        return true
    }


    private fun showPermissionDeniedDialog(activity:  ContactsActivity) {
        AlertDialog.Builder(activity)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    activity.startActivity(intent)
                })
            .setNegativeButton("Cancel", null)
            .show()
    }

}
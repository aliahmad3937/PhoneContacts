package com.cc.phonecontacts.ui.contacts.usecase

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.cc.phonecontacts.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ReadContactsUseCase @Inject constructor() {

      @SuppressLint("Range")
      operator fun invoke(ctx: Context): Flow<Contact> = flow {
        Log.e("ReadContactsUseCase", "Thread name : ${Thread.currentThread().name}")
        val contentResolver = ctx.contentResolver
          val order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        val cursor: Cursor? =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, order)
        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val info = Contact()
                val id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )


                    val person: Uri =
                        ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            id.toLong()
                        )


                    val crEmails = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = ?",
                        arrayOf(id),
                        null
                    )
                    if(crEmails != null) {
                        while (crEmails!!.moveToNext()) {
                            info.gmail = crEmails.getString(
                                crEmails
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                            )
                        }
                    }
                    crEmails?.close()


                    val bdc = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        arrayOf<String>(ContactsContract.CommonDataKinds.Event.DATA),
                        ContactsContract.Data.CONTACT_ID + " = " + id + " AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' AND " + ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                        null,
                        ContactsContract.Data.DISPLAY_NAME
                    )
                    if (bdc != null && bdc.count > 0) {
                        while (bdc.moveToNext()) {
                            info.dob = bdc.getString(0)
                            // now "id" is the user's unique ID, "name" is his full name and "birthday" is the date and time of his birth
                        }
                    }

                    bdc?.close()

                    while (cursorInfo!!.moveToNext()) {
                        info.id = id
                        info.name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        info.mobileNumber =
                            cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        info.photoURI = person
                        emit(info)
                    }
                    cursorInfo.close()
                }
            }
            cursor.close()
        }
    }
}


//
//    var contactList: ArrayList<Contact> = ArrayList()
//
//    private val PROJECTION = arrayOf(
//        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
//        ContactsContract.Contacts.DISPLAY_NAME,
//        ContactsContract.CommonDataKinds.Phone.NUMBER
//    )
//
//    private fun getContactList() {
//        val cr: ContentResolver = getContentResolver()
//        val cursor = cr.query(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//            PROJECTION,
//            null,
//            null,
//            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
//        )
//        if (cursor != null) {
//            val mobileNoSet = HashSet<String>()
//            try {
//                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
//                val numberIndex =
//                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
//                var name: String
//                var number: String
//                while (cursor.moveToNext()) {
//                    name = cursor.getString(nameIndex)
//                    number = cursor.getString(numberIndex)
//                    number = number.replace(" ", "")
//                    if (!mobileNoSet.contains(number)) {
//                        contactList.add(Contact(name, number))
//                        mobileNoSet.add(number)
//                        Log.d(
//                            "hvy", "onCreaterrView  Phone Number: name = " + name
//                                    + " No = " + number
//                        )
//                    }
//                }
//            } finally {
//                cursor.close()
//            }
//        }
//    }
//}
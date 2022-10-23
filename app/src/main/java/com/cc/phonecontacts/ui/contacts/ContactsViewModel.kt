package com.cc.phonecontacts.ui.contacts

import android.content.Context
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.cc.phonecontacts.adapter.ContactAdapter
import com.cc.phonecontacts.model.Contact

import com.cc.phonecontacts.ui.contacts.usecase.ReadContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val readContactsUseCase: ReadContactsUseCase,
    @ApplicationContext val application: Context,
) : ViewModel() {

    private val TAG = "ContactsViewModel"
    var contactList: ArrayList<Contact> = arrayListOf()


    fun getContacts( listener :() -> Unit) {
        if (contactList.isEmpty()) {
            viewModelScope.launch {
                readContactsUseCase(application)// Execute on the io dispatcher
// flowOn affects the upstream flow ↑
                    .flowOn(Dispatchers.IO)
// the downstream flow ↓ is not affected
                    .catch { exception -> // Executes in the consumer's context
                        Log.e(TAG,exception.localizedMessage)
                        emit(Contact())
                    }.collect {
                        contactList.add(it)
                        listener()
                    }
            }
        }
    }


}
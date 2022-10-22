package com.cc.phonecontacts.ui.contacts

import android.content.Context
import androidx.lifecycle.ViewModel
import com.cc.phonecontacts.model.Contact

import com.cc.phonecontacts.ui.contacts.usecase.ReadContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val readContactsUseCase: ReadContactsUseCase,
    @ApplicationContext val application: Context,
) : ViewModel() {

    private val TAG = "ContactsViewModel"

    fun getContacts():Flow<Contact> = readContactsUseCase(application)// Execute on the io dispatcher
// flowOn affects the upstream flow ↑
        .flowOn(Dispatchers.IO)
// the downstream flow ↓ is not affected
        .catch { exception -> // Executes in the consumer's context
            emit(Contact())
        }


}
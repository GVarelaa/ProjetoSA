package com.example.elderwatch.ui.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elderwatch.utils.UserManager
import com.google.firebase.firestore.FirebaseFirestore

class ContactsViewModel : ViewModel() {
    val contacts = MutableLiveData<MutableList<Any?>>()

    init {
        fetchData()
    }

    fun fetchData() {
        contacts.value = UserManager.contacts as MutableList<Any?>?
    }
    fun addContact(newContact: String) {
        contacts.value?.plusAssign(newContact)
    }
}
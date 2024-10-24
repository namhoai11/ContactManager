package com.example.contactmanager.data

import android.content.Context

interface AppContainer {
    val contactsRepository: ContactsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val contactsRepository: ContactsRepository by lazy {
        OfflineContactsRepository(ContactDatabase.getDatabase(context).contactDao(), context)
    }
}
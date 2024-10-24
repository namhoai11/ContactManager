package com.example.contactmanager.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.contactmanager.ContactManagerApplication
import com.example.contactmanager.ui.contact.detail.ContactDetailsViewModel
import com.example.contactmanager.ui.contact.edit.ContactEditViewModel
import com.example.contactmanager.ui.contact.entry.ContactEntryViewModel
import com.example.contactmanager.ui.home.HomeViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(contactManagerApplication().container.contactsRepository)
        }
        initializer {
            ContactEntryViewModel(contactManagerApplication().container.contactsRepository)
        }
        initializer {
            ContactDetailsViewModel(
                this.createSavedStateHandle(),
                contactManagerApplication().container.contactsRepository
            )
        }
        initializer {
            ContactEditViewModel(
                this.createSavedStateHandle(),
                contactManagerApplication().container.contactsRepository
            )
        }
    }
}

fun CreationExtras.contactManagerApplication(): ContactManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ContactManagerApplication)
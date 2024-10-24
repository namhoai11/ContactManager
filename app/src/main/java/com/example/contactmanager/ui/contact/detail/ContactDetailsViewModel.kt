package com.example.contactmanager.ui.contact.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactmanager.data.ContactsRepository
import com.example.contactmanager.ui.uistate.ContactDetailsUIState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ContactDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val contactId: Int =
        checkNotNull(savedStateHandle[ContactDetailsDestination.contactIdArg])

    val uiState: StateFlow<ContactDetailsUIState> = contactsRepository.getContactStream(contactId)
        .filterNotNull()
        .map { ContactDetailsUIState(contactDetails = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ContactDetailsUIState()
        )

    suspend fun deleteContact() {
        contactsRepository.deleteContact(uiState.value.contactDetails)
    }
}
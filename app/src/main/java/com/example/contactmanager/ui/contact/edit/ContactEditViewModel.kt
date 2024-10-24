package com.example.contactmanager.ui.contact.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactmanager.data.Contact
import com.example.contactmanager.data.ContactsRepository
import com.example.contactmanager.ui.contact.entry.toUIState
import com.example.contactmanager.ui.uistate.ContactUIState
//import com.google.androidbrowserhelper.playbilling.digitalgoods.ItemDetails
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ContactEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    var contactUiState by mutableStateOf(ContactUIState())
        private set
    private val contactId: Int = checkNotNull(savedStateHandle[ContactEditDestination.contactIdArg])

    private fun validateInput(uiState: Contact = contactUiState.contactDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()
        }
    }

    init {
        viewModelScope.launch {
            contactUiState = contactsRepository.getContactStream(contactId)
                .filterNotNull()
                .first()
                .toUIState()
        }
    }

    suspend fun updateContact(): Result<Unit> {
        return if (validateInput()) {
            val result = contactsRepository.editContact(contactUiState.contactDetails)
            if (result.isFailure) {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                contactUiState = contactUiState.copy(errorMessage = errorMessage)
                Result.failure(Throwable(errorMessage))
            } else {
                contactUiState = contactUiState.copy(errorMessage = null)
                Result.success(Unit)
            }
        }else {
            Result.failure(Throwable("Invalid input"))
        }
    }


    fun updateUiState(contactDetails: Contact) {
        contactUiState = ContactUIState(
            contactDetails = contactDetails,
            isEntryValid = validateInput(contactDetails)
        )
    }
}
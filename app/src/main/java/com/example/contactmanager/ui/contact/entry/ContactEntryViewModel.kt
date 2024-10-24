package com.example.contactmanager.ui.contact.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.contactmanager.data.Contact
import com.example.contactmanager.data.ContactsRepository
import com.example.contactmanager.ui.uistate.ContactUIState

class ContactEntryViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {
    var contactUiState by mutableStateOf(ContactUIState())
        private set

    fun updateUiState(contact: Contact) {
        contactUiState =
            ContactUIState(contactDetails = contact, isEntryValid = validateInput(contact))
    }

    private fun validateInput(uiState: Contact = contactUiState.contactDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()
        }
    }

    suspend fun saveContact(): Result<Unit> {
        return if (validateInput()) {
            val result = contactsRepository.insertContact(contactUiState.contactDetails)
            if (result.isFailure) {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                contactUiState = contactUiState.copy(errorMessage = errorMessage)
                Result.failure(Throwable(errorMessage))
            } else {
                contactUiState = contactUiState.copy(errorMessage = null)
                Result.success(Unit)
            }
        } else {
            Result.failure(Throwable("Invalid input"))
        }
    }
}

fun Contact.toUIState(isEntryValid: Boolean = false): ContactUIState {
    return ContactUIState(
        contactDetails = this,
        isEntryValid = isEntryValid
    )
}

//dùng Flow

//class ContactEntryViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {
//    private val _contactUiState = MutableStateFlow(ContactDetailsUIState())
//    val contactUiState: StateFlow<ContactDetailsUIState> = _contactUiState
//
//    // Cập nhật trạng thái UI
//    fun updateUiState(contact: Contact) {
//        _contactUiState.value = ContactDetailsUIState(contactDetails = contact, isEntryValid = validateInput(contact))
//    }
//    fun updateUiState(itemDetails: ItemDetails) {
//    _itemUiState.update { currentState ->
//        currentState.copy(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
//    }
//}
//
//    // Hàm xác thực thông tin nhập vào
//    private fun validateInput(uiState: Contact = _contactUiState.value.contactDetails): Boolean {
//        return with(uiState) {
//            name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()
//        }
//    }
//
//    // Hàm lưu Contact
//    fun saveContact() {
//        if (validateInput()) {
//            viewModelScope.launch {
//                contactsRepository.insertContact(_contactUiState.value.contactDetails) // Thêm contact vào repository
//            }
//        }
//    }
//}
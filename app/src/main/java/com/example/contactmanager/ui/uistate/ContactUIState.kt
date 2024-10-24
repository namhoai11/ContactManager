package com.example.contactmanager.ui.uistate

import com.example.contactmanager.data.Contact

data class ContactUIState(
    val contactDetails: Contact = Contact(
        id = 0,
        name = "",
        email = "",
        phone = ""
    ),
    val isEntryValid: Boolean = false,
    val errorMessage: String? = null
)
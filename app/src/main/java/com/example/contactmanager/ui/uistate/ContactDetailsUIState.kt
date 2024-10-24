package com.example.contactmanager.ui.uistate

import com.example.contactmanager.data.Contact

data class ContactDetailsUIState(
    val contactDetails: Contact = Contact(
        id = 0,
        name = "",
        email = "",
        phone = ""
    )
)
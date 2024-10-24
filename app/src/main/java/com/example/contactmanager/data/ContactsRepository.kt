package com.example.contactmanager.data

import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getAllContactsStream(): Flow<List<Contact>>
    fun getContactStream(id: Int): Flow<Contact?>
    suspend fun insertContact(contact: Contact): Result<Unit>
    suspend fun deleteContact(contact: Contact)
    suspend fun editContact(contact: Contact): Result<Unit>

    // Thêm hàm để đọc và chèn danh bạ từ file JSON
    suspend fun populateContactsFromJson(fileName: String): Result<Unit>

    fun getContactsByNameStream(name: String): Flow<List<Contact>>
}
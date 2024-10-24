package com.example.contactmanager.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


class OfflineContactsRepository(
    private val contactDao: ContactDao,
    private val context: Context
) : ContactsRepository {
    override fun getAllContactsStream(): Flow<List<Contact>> = contactDao.getAllContacts()
    override fun getContactStream(id: Int): Flow<Contact?> =contactDao.getContactById(id)

    override suspend fun insertContact(contact: Contact): Result<Unit> {
        val existingContact = contactDao.getContactByEmailAndPhone(contact.email, contact.phone)
        return if (existingContact == null) {
            contactDao.insert(contact)
            Result.success(Unit)
        } else {
            Result.failure(Throwable("Contact with same email and phone already exists"))
        }
    }

    override suspend fun editContact(contact: Contact): Result<Unit> {
        // Kiểm tra sự tồn tại trước khi cập nhật
        val existingContact = contactDao.getContactByEmailAndPhone(contact.email, contact.phone)
        return if (existingContact == null || existingContact.id == contact.id) {
            contactDao.update(contact)
            Result.success(Unit)
        } else {
            Result.failure(Throwable("Contact with same email and phone already exists"))
        }
    }

    override suspend fun deleteContact(contact: Contact) = contactDao.delete(contact)

    override suspend fun populateContactsFromJson(fileName: String): Result<Unit> {
        return try {
            Log.d("ContactsRepository", "Parsing contacts from JSON file: $fileName")
            val contacts = parseContactsFromJson(fileName)
            contacts.forEach { contact ->
                Log.d("ContactsRepository", "Inserting contact: $contact")
                insertContact(contact)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ContactsRepository", "Error parsing contacts from JSON", e)
            Result.failure(e)
        }
    }
    private suspend fun parseContactsFromJson(fileName: String): List<Contact> {
        return withContext(Dispatchers.IO) {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            Log.d("ContactsRepository", "JSON String: $jsonString")
            Json.decodeFromString(jsonString)
        }
    }

    override fun getContactsByNameStream(name: String): Flow<List<Contact>> {
        return contactDao.getContactsByName(name)
    }
}
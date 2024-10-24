package com.example.contactmanager.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * from contacts WHERE email = :email AND phone = :phone LIMIT 1")
    suspend fun getContactByEmailAndPhone(email: String, phone: String): Contact?

    @Query("SELECT * from contacts ORDER BY name ASC")
    fun getAllContacts(): Flow<List<Contact>>

    @Query("SELECT*FROM contacts WHERE id=:id")
    fun getContactById(id: Int): Flow<Contact>

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :name || '%'")
    fun getContactsByName(name: String): Flow<List<Contact>>

}
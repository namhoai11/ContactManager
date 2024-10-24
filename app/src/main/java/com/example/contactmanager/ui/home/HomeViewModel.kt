package com.example.contactmanager.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactmanager.data.ContactsRepository
import com.example.contactmanager.ui.uistate.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val _searchQuery = mutableStateOf("")

    val searchQuery: String
        get() = _searchQuery.value

    val homeUiState: StateFlow<HomeUiState> =
        contactsRepository.getAllContactsStream().map { HomeUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Khởi tạo
    init {
        viewModelScope.launch {
            val result = contactsRepository.populateContactsFromJson("contact_test.json")
            Log.d("HomeViewModel", "Result of populateContactsFromJson: $result") // Log kết quả
        }
    }

    fun searchContacts(): StateFlow<HomeUiState> {
        return contactsRepository.getContactsByNameStream(_searchQuery.value).map {
            HomeUiState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )
    }

}
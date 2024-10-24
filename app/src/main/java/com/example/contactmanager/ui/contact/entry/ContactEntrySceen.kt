package com.example.contactmanager.ui.contact.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
//import coil.EventListener
import com.example.contactmanager.ContactManagerTopAppBar
import com.example.contactmanager.R
import com.example.contactmanager.data.Contact
import com.example.contactmanager.ui.AppViewModelProvider
import com.example.contactmanager.ui.navigation.NavigationDestination
import com.example.contactmanager.ui.theme.ContactManagerTheme
import com.example.contactmanager.ui.uistate.ContactUIState
import kotlinx.coroutines.launch


object ContactEntryDestination : NavigationDestination {
    override val route = "contact_entry"
    override val titleRes = R.string.contact_entry_title
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ContactEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.contactUiState
    Scaffold(
        topBar = {
            ContactManagerTopAppBar(
                title = stringResource(ContactEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ContactEntryBody(
            contactDetailsUIState = uiState,
            onContactValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    val result = viewModel.saveContact()

                    if (result.isSuccess) {
                        navigateBack()
                    }
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )

    }
}

@Composable
fun ContactEntryBody(
    contactDetailsUIState: ContactUIState,
    onContactValueChange: (Contact) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ContactInputForm(
            contact = contactDetailsUIState.contactDetails,
            onvalueChange = onContactValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        // Hiển thị thông báo lỗi nếu có
            contactDetailsUIState.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
        Button(
            onClick = onSaveClick,
            enabled = contactDetailsUIState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@Composable
fun ContactInputForm(
    contact: Contact,
    modifier: Modifier = Modifier,
    onvalueChange: (Contact) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = contact.name,
            onValueChange = { onvalueChange(contact.copy(name = it)) },
            label = { Text(text = stringResource(id = R.string.contact_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = contact.email,
            onValueChange = { onvalueChange(contact.copy(email = it)) },
            label = { Text(text = stringResource(id = R.string.contact_email_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = contact.phone,
            onValueChange = { onvalueChange(contact.copy(phone = it)) },
            label = { Text(text = stringResource(id = R.string.contact_phone_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun ContactEntryScreenPreview() {
    ContactManagerTheme {
        ContactEntryBody(contactDetailsUIState = ContactUIState(
            Contact(1, "Nam Hoai", "hoainamcs2002@gmail.com", "0359232204"),

            ),
            onContactValueChange = {},
            onSaveClick = {})
    }
}
package com.example.contactmanager.ui.contact.edit

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactmanager.ContactManagerTopAppBar
import com.example.contactmanager.R
import com.example.contactmanager.ui.AppViewModelProvider
import com.example.contactmanager.ui.contact.entry.ContactEntryBody
import com.example.contactmanager.ui.contact.entry.ContactEntryDestination
import com.example.contactmanager.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object ContactEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_contact_title
    const val contactIdArg = "contactId"
    val routeWithArgs = "$route/{$contactIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
//    modifier: Modifier = Modifier,
    viewModel: ContactEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
    ){innerPadding ->
        ContactEntryBody(
            contactDetailsUIState = uiState,
            onContactValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    val result=viewModel.updateContact()
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
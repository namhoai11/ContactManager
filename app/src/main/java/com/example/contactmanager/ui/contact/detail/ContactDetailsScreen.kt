package com.example.contactmanager.ui.contact.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactmanager.ContactManagerTopAppBar
import com.example.contactmanager.R
import com.example.contactmanager.data.Contact
import com.example.contactmanager.ui.AppViewModelProvider
import com.example.contactmanager.ui.navigation.NavigationDestination
import com.example.contactmanager.ui.theme.ContactManagerTheme
import com.example.contactmanager.ui.uistate.ContactDetailsUIState
import kotlinx.coroutines.launch


object ContactDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.contact_detail_title
    const val contactIdArg = "contactId"
    val routeWithArgs = "$route/{$contactIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContactDetailsViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            ContactManagerTopAppBar(
                title = stringResource(id = ContactDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }, modifier = modifier
    ) { innerPadding ->
        ContactDetailsBody(
            contactDetailsUIState = uiState.value,
            onEditContact = { navigateToEditItem(uiState.value.contactDetails.id) },
            onDeleteContact = {
                coroutineScope.launch {
                    viewModel.deleteContact()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun ContactDetailsBody(
    contactDetailsUIState: ContactDetailsUIState,
    onEditContact: () -> Unit,
    onDeleteContact: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        ContactDetails(
            contact = contactDetailsUIState.contactDetails,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onEditContact,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = true
        ) {
            Text(text = stringResource(id = R.string.edit))
        }
        Button(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text(text = stringResource(id = R.string.delete), color = Color.White)
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDeleteContact()
                },
                onDeleteCancel = { deleteConfirmationRequired = false })
        }
    }
}

@Composable
fun ContactDetails(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            ContactDetailsRow(
                labelResID = R.string.name,
                contactDetail = contact.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            ContactDetailsRow(
                labelResID = R.string.email,
                contactDetail = contact.email,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            ContactDetailsRow(
                labelResID = R.string.phone,
                contactDetail = contact.phone,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }
    }
}

@Composable
private fun ContactDetailsRow(
    @StringRes labelResID: Int, contactDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = contactDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    ContactManagerTheme {
        ContactDetailsBody(
            ContactDetailsUIState(
                contactDetails = Contact(1, "Nam Hoai", "hoainamcs2002@gmail.com", "0359232204")
            ),
            onEditContact = {},
            onDeleteContact = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteConfirmationDialogPreview() {
    // Giá trị giả lập cho các tham số
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                // Xử lý khi xác nhận xóa
                showDialog = false
            },
            onDeleteCancel = {
                // Xử lý khi hủy xóa
                showDialog = false
            }
        )
    }
}
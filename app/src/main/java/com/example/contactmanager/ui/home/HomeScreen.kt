package com.example.contactmanager.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contactmanager.R
import com.example.contactmanager.data.Contact
import com.example.contactmanager.ui.navigation.NavigationDestination
import com.example.contactmanager.ui.theme.ContactManagerTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactmanager.ContactManagerTopAppBar
import com.example.contactmanager.ui.AppViewModelProvider


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToContactEntry: () -> Unit,
    navigateToContactDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
//    val homeUiState by viewModel.homeUiState.collectAsState()
//    val homeUiState by viewModel.searchContacts().collectAsState()
    val homeUiState by if (viewModel.searchQuery.isEmpty()) {
        viewModel.homeUiState.collectAsState() // Lấy toàn bộ danh sách khi không tìm kiếm
    } else {
        viewModel.searchContacts().collectAsState() // Chỉ tìm kiếm khi có query
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ContactManagerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onSearchQueryChanged = { query ->
                    viewModel.updateSearchQuery(query)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToContactEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.contact_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            contactList = homeUiState.contactList,
            onContactClick = navigateToContactDetail,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding
        )
    }
}

@Composable
fun HomeBody(
    contactList: List<Contact>,
    onContactClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (contactList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_contact_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            ContactList(
                contactList = contactList,
                onContactClick = onContactClick,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun ContactList(
    contactList: List<Contact>,
    onContactClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = contactList, key = { it.id }) { contact ->
            ContactItem(
                contact = contact,
                onContactClick = onContactClick,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )

        }

    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onContactClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { onContactClick(contact.id) }
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small)),

            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = contact.email,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = contact.phone,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListPreview() {
    ContactManagerTheme {
        ContactList(
            contactList = listOf(
                Contact(1, "Nam Hoai", "hoainamcs2002@gmail.com", "0359232204"),
                Contact(2, "Nam Hoai", "hoainamcs2002@gmail.com", "0359232204"),
                Contact(3, "Nam Hoai", "hoainamcs2002@gmail.com", "0359232204"),
            ),
            onContactClick = {},
//            contentPadding =  PaddingValues(0.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    ContactManagerTheme {
        ContactItem(
            Contact(1, "Nam Hoai", "hoainamcs2002@gmail.com", "0359232204"),
            onContactClick = {}
        )
    }
}
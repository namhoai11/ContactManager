package com.example.contactmanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.contactmanager.ui.contact.detail.ContactDetailsDestination
import com.example.contactmanager.ui.contact.detail.ContactDetailsScreen
import com.example.contactmanager.ui.contact.edit.ContactEditDestination
import com.example.contactmanager.ui.contact.edit.ContactEditScreen
import com.example.contactmanager.ui.contact.entry.ContactEntryDestination
import com.example.contactmanager.ui.contact.entry.ContactEntryScreen
import com.example.contactmanager.ui.home.HomeDestination
import com.example.contactmanager.ui.home.HomeScreen


@Composable
fun ContactManagerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToContactEntry = { navController.navigate(ContactEntryDestination.route) },
                navigateToContactDetail = {
                    navController.navigate("${ContactDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ContactEntryDestination.route) {
            ContactEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ContactDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ContactDetailsDestination.contactIdArg) {
                type = NavType.IntType
            })
        ) {
            ContactDetailsScreen(
                navigateToEditItem = { navController.navigate("${ContactEditDestination.route}/${it}") },
                navigateBack = { navController.popBackStack() },
                navigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ContactEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ContactEditDestination.contactIdArg) {
                type = NavType.IntType
            })
        ) {
            ContactEditScreen(navigateBack = { navController.popBackStack() }, onNavigateUp = { navController.navigateUp() })
        }
    }
}

package com.example.allapp.ui.theme.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.allapp.ViewModel.AccountViewModel
import com.example.allapp.ViewModel.AccountViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(context))
    val currentUser by viewModel.currentUser.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "FixItNow",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Profile",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Welcome, ${currentUser?.name}!",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Role: ${currentUser?.role}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Email: ${currentUser?.email}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Menu Options
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ListItem(
                        headlineContent = { Text("Contact Us") },
                        leadingContent = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Contact"
                            )
                        },
                        modifier = Modifier.clickable { navController.navigate("contact") }
                    )

                    Divider()

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign Out")
                    }
                }
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Sign Out") },
                    text = { Text("Are you sure you want to sign out?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.logout()
                                showLogoutDialog = false
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        ) {
                            Text("Sign Out")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
} 
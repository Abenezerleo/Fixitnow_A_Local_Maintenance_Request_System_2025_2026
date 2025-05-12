package com.example.allapp.ui.theme.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.allapp.Model.ServiceType
import com.example.allapp.Model.UserProfile
import com.example.allapp.Model.UserUpdate
import com.example.allapp.ViewModel.ProviderViewModel
import com.example.allapp.ViewModel.ProviderViewModelFactory

// Colors (inline)
val LightGray = Color(0xFFD3D3D3)
val Red = Color(0xFFFF0000)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Green = Color(0xFF006400)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderScreen(
    viewModel: ProviderViewModel = viewModel(
        factory = ProviderViewModelFactory()
    )
) {
    val providers by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<UserProfile?>(null) }
    var showDeleteDialog by remember { mutableStateOf<UserProfile?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Text(
            text = "Providers",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    viewModel.searchProviders(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Search Provider...") },
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray.copy(alpha = 0.2f),
                    unfocusedContainerColor = LightGray.copy(alpha = 0.2f)
                )
            )

            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "ADD PROVIDER", color = White)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Available Service Providers",
                color = Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            LazyColumn {
                items(providers) { provider ->
                    ProviderCard(
                        provider = provider,
                        onEdit = { showEditDialog = provider },
                        onDelete = { showDeleteDialog = provider }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Add Provider Dialog
    if (showAddDialog) {
        AddProviderDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, email, password, phone, gender, serviceType ->
                val newProvider = UserProfile(
                    name = name,
                    email = email,
                    password = password,
                    phone = phone,
                    gender = gender,
                    role = "PROVIDER",
                    serviceType = serviceType,
                    bankName = null,
                    accountNumber = null,
                    accountName = null,
                    cbeAccount = null,
                    paypalAccount = null,
                    telebirrAccount = null,
                    awashAccount = null
                )
                viewModel.createProvider(newProvider)
                showAddDialog = false
            }
        )
    }

    // Edit Provider Dialog
    showEditDialog?.let { provider ->
        EditProviderDialog(
            provider = provider,
            onDismiss = { showEditDialog = null },
            onConfirm = { name, email, phone, gender, serviceType ->
                val update = UserUpdate(
                    name = name,
                    email = email,
                    phone = phone,
                    gender = gender,
                    serviceType = serviceType
                )
                provider.id?.let { id ->
                    viewModel.updateProvider(id, update)
                }
                showEditDialog = null
            }
        )
    }

    // Delete Provider Dialog
    showDeleteDialog?.let { provider ->
        DeleteProviderDialog(
            provider = provider,
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                provider.id?.let { id ->
                    viewModel.deleteProvider(id)
                }
                showDeleteDialog = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderCard(
    provider: UserProfile,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(LightGray.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                        Text(
                            text = if (provider.gender.equals("MALE", ignoreCase = true)) "👨" else "👩",
                            style = MaterialTheme.typography.headlineMedium
                        )
                }
            }

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = provider.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                        text = provider.serviceType ?: "No service type",
                    style = MaterialTheme.typography.bodyMedium,
                        color = Black
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onEdit,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                border = BorderStroke(1.dp, LightGray),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Edit", color = Black)
            }

            Button(
                onClick = onDelete,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                border = BorderStroke(1.dp, LightGray),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Delete", color = Black)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProviderDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, password: String, phone: String, gender: String, serviceType: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var serviceType by remember { mutableStateOf(ServiceType.CLEANING.name) }
    var genderExpanded by remember { mutableStateOf(false) }
    var serviceTypeExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("MALE", "FEMALE")
    val serviceTypeOptions = ServiceType.values().map { it.name }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Provider") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = serviceTypeExpanded,
                    onExpandedChange = { serviceTypeExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = serviceType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Service Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceTypeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = serviceTypeExpanded,
                        onDismissRequest = { serviceTypeExpanded = false }
                    ) {
                        serviceTypeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    serviceType = option
                                    serviceTypeExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, email, password, phone, gender, serviceType)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProviderDialog(
    provider: UserProfile,
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, phone: String, gender: String, serviceType: String) -> Unit
) {
    var name by remember { mutableStateOf(provider.name) }
    var email by remember { mutableStateOf(provider.email) }
    var phone by remember { mutableStateOf(provider.phone) }
    var gender by remember { mutableStateOf(provider.gender) }
    var serviceType by remember { mutableStateOf(provider.serviceType ?: ServiceType.CLEANING.name) }
    var genderExpanded by remember { mutableStateOf(false) }
    var serviceTypeExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("MALE", "FEMALE")
    val serviceTypeOptions = ServiceType.values().map { it.name }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Provider") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    genderExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = serviceTypeExpanded,
                    onExpandedChange = { serviceTypeExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = serviceType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Service Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceTypeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = serviceTypeExpanded,
                        onDismissRequest = { serviceTypeExpanded = false }
                    ) {
                        serviceTypeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    serviceType = option
                                    serviceTypeExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, email, phone, gender, serviceType)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteProviderDialog(
    provider: UserProfile,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Provider") },
        text = { Text("Are you sure you want to delete ${provider.name}?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

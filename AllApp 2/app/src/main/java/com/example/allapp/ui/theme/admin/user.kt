package com.example.allapp.ui.theme.admin

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.allapp.Model.UserProfile
import com.example.allapp.Model.UserUpdate
import com.example.allapp.ViewModel.UserViewModel
import com.example.allapp.ViewModel.UserViewModelFactory

@Composable
fun UserScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: UserViewModel = viewModel(factory = UserViewModelFactory(context))
    val requesters by viewModel.requesters.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val error by viewModel.error.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<UserProfile?>(null) }
    var showDeleteDialog by remember { mutableStateOf<UserProfile?>(null) }
    var dialogError by remember { mutableStateOf<String?>(null) }

    // Show error toast
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = "Requesters",
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
                    viewModel.searchRequesters(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Search requesters...") },
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(red = 0, green = 100, blue = 0)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "ADD REQUESTER", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn {
                    items(searchResults) { userWithReport ->
                        UserCard(
                            userWithReport = userWithReport,
                            onEdit = { showEditDialog = userWithReport.user },
                            onDelete = { showDeleteDialog = userWithReport.user }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // Add Requester Dialog
    if (showAddDialog) {
        AddEditRequesterDialog(
            onDismiss = { showAddDialog = false },
            onSave = { userProfile ->
                viewModel.createRequester(userProfile)
                showAddDialog = false
            },
            dialogError = dialogError,
            setDialogError = { dialogError = it }
        )
    }

    // Edit Requester Dialog
    showEditDialog?.let { user ->
        AddEditRequesterDialog(
            user = user,
            onDismiss = { showEditDialog = null },
            onSave = { userProfile ->
                user.id?.let { id ->
                    viewModel.updateRequester(id, UserUpdate(
                        name = userProfile.name,
                        email = userProfile.email,
                        phone = userProfile.phone,
                        gender = userProfile.gender
                    ))
                }
                showEditDialog = null
            },
            dialogError = dialogError,
            setDialogError = { dialogError = it }
        )
    }

    // Delete User Dialog
    showDeleteDialog?.let { user ->
        DeleteUserDialog(
            user = user,
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                user.id?.let { id ->
                    viewModel.deleteRequester(id)
                }
                showDeleteDialog = null
            }
        )
    }
}

@Composable
fun UserCard(
    userWithReport: UserViewModel.UserWithReport,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val user = userWithReport.user
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDCDCDC)
        )
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
                        .background(Color.Gray.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (user.gender.equals("MALE", ignoreCase = true)) "👨" else "👩",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Column(
                modifier = Modifier.weight(2f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRequesterDialog(
    user: UserProfile? = null,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit,
    dialogError: String?,
    setDialogError: (String?) -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var gender by remember { mutableStateOf(user?.gender ?: "MALE") }
    var password by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("MALE", "FEMALE")
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Add Requester" else "Edit Requester") },
        text = {
            Column {
                dialogError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = name.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = email.isBlank() || !email.contains("@")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = phone.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Gender") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        isError = gender.isBlank()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                if (user == null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = password.isBlank()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        setDialogError("Name is required")
                    } else if (email.isBlank() || !email.contains("@")) {
                        setDialogError("Valid email is required")
                    } else if (phone.isBlank()) {
                        setDialogError("Phone is required")
                    } else if (gender.isBlank()) {
                        setDialogError("Gender is required")
                    } else if (user == null && password.isBlank()) {
                        setDialogError("Password is required")
                    } else {
                        setDialogError(null)
                        val newUser = UserProfile(
                            id = user?.id ?: "0",
                            name = name.trim(),
                            email = email.trim(),
                            phone = phone.trim(),
                            gender = gender.trim(),
                            role = "REQUESTER",
                            password = if (user == null) password.trim() else null,
                            serviceType = null,
                            bankName = null,
                            accountNumber = null,
                            accountName = null,
                            cbeAccount = null,
                            paypalAccount = null,
                            telebirrAccount = null,
                            awashAccount = null
                        )
                        onSave(newUser)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteUserDialog(
    user: UserProfile,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete User") },
        text = { Text("Are you sure you want to delete ${user.name}?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
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

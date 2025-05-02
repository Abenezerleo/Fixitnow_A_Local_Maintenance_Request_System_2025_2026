package com.example.assigment.ui.theme.pages

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assigment.ui.theme.Entity.Account
import com.example.assigment.ui.theme.ViewModel.UserViewModel
import com.example.assigment.ui.theme.others.AddUserScreen
import com.example.assigment.ui.theme.others.EditUserScreen

// State to control which page inside UserScreen
sealed class UserPageState {
    object List : UserPageState()
    object Add : UserPageState()
    data class Edit(val user: Account) : UserPageState()
}

@Composable
fun UserScreen() {
    val viewModel: UserViewModel = viewModel()
    val users by viewModel.accounts.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var userPageState by remember { mutableStateOf<UserPageState>(UserPageState.List) }

    when (val state = userPageState) {
        is UserPageState.List -> {
            // Main List Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Text(
                    text = "Users",
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
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text("Search users...") },
                        shape = CircleShape,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f)
                        )
                    )

                    Button(
                        onClick = { userPageState = UserPageState.Add },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(red = 0, green = 100, blue = 0)
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add User")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add User")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn {
                        items(users.filter { 
                            it.fullName?.contains(searchQuery, ignoreCase = true) == true ||
                            it.email?.contains(searchQuery, ignoreCase = true) == true
                        }) { user ->
                            UserCard(
                                user = user,
                                onEdit = { userPageState = UserPageState.Edit(user) },
                                onDelete = { viewModel.deleteUser(user.accountId) }
                            )
                        }
                    }
                }
            }
        }
        is UserPageState.Add -> {
            AddUserScreen(
                onAddUser = { fullName, email, phoneNumber, password, gender ->
                    viewModel.addUser(
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        gender = gender
                    )
                    userPageState = UserPageState.List
                },
                onCancel = { userPageState = UserPageState.List }
            )
        }
        is UserPageState.Edit -> {
            val user = state.user
            EditUserScreen(
                user = user,
                onUpdateUser = { fullName, email, phoneNumber, password, gender ->
                    viewModel.updateUser(
                        accountId = user.accountId,
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        gender = gender
                    )
                    userPageState = UserPageState.List
                },
                onCancel = { userPageState = UserPageState.List }
            )
        }
    }
}

@Composable
fun UserCard(
    user: Account,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (user.gender?.contains("Male") == true) "👨" else "👩",
                        fontSize = 24.sp
                    )}
                Column {
                    Text(
                        text = user.fullName ?: "Unknown",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = user.email ?: "No email",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )}
                Column {
                    Text(
                        text = user.phoneNumber ?: "No phone",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Column {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit User")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete User")
                    }
                }
            }
        }
    }
}

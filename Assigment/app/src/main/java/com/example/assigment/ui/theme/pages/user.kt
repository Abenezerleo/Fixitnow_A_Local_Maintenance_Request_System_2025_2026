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
import com.example.assigment.ui.theme.Entity.User as RoomUser
import com.example.assigment.ui.theme.ViewModel.UserViewModel
import com.example.assigment.ui.theme.others.AddUserScreen
import com.example.assigment.ui.theme.others.EditUserScreen

// State to control which page inside UserScreen
sealed class UserPageState {
    object List : UserPageState()
    object Add : UserPageState()
    data class Edit(val user: RoomUser) : UserPageState()
}

@Composable
fun UserScreen() {
    val viewModel: UserViewModel = viewModel()
    val users by viewModel.allUsers.collectAsState(initial = emptyList())
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
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "ADD USER", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn {
                        val filteredUsers = users.filter {
                            it.name.contains(searchQuery, ignoreCase = true) ||
                                    it.email.contains(searchQuery, ignoreCase = true) ||
                                    it.phoneNumber?.contains(searchQuery, ignoreCase = true) == true
                        }

                        items(filteredUsers) { user ->
                            UserCard(
                                user = user,
                                onEdit = { userPageState = UserPageState.Edit(user) },
                                onDelete = { viewModel.deleteUser(user) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        is UserPageState.Add -> {
            AddUserScreen(
                onSave = { newUser ->
                    viewModel.addUser(newUser)
                    userPageState = UserPageState.List
                },
                onCancel = { userPageState = UserPageState.List }
            )
        }

        is UserPageState.Edit -> {
            EditUserScreen(
                user = state.user,
                onSave = { updatedUser ->
                    viewModel.updateUser(updatedUser)
                    userPageState = UserPageState.List
                },
                onCancel = { userPageState = UserPageState.List }
            )
        }
    }
}

@Composable
fun UserCard(user: RoomUser, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // 1. Emoji
            Column(
                modifier = Modifier.weight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF5F5F5), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = user.emoji, fontSize = 28.sp, color = Color.Black)
                }
            }
            // 2. Name (top), Email (bottom)
            Column(
                modifier = Modifier.weight(2f).padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
            // 3. Phone (top), Reported (bottom)
            Column(
                modifier = Modifier.weight(1.2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.phoneNumber ?: "No phone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Reported: " + if (user.reported.equals("yes", true) || user.reported.equals("true", true)) "Yes" else "No",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (user.reported.equals("yes", true) || user.reported.equals("true", true)) Color.Red else Color.Black
                )
            }
            // 4. Edit (top), Delete (bottom)
            Column(
                modifier = Modifier.weight(0.8f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = if (user.reported.equals("yes", true) || user.reported.equals("true", true)) Color.Red else Color.Black
                    )
                }
            }
        }
    }
}

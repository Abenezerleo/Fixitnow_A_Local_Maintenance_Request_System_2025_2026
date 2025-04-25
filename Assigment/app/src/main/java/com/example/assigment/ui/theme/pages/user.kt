package com.example.assigment.ui.theme.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

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

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val emoji: String,
    var reported: Boolean
)

@Composable
fun UserScreen() {
    val users = remember {
        mutableStateListOf(
            User(1, "John Doe", "john@example.com", "+123456789", "👨", false),
            User(2, "Jane Smith", "jane@example.com", "+987654321", "👩", true),
            User(3, "Bob Johnson", "bob@example.com", "+1122334455", "👨", false)
        )
    }

    var searchQuery by remember { mutableStateOf("") }

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
                onClick = { /* Add User */ },
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
                items(users) { user ->
                    UserCard(
                        user = user,
                        onReportToggle = {
                            val index = users.indexOfFirst { it.id == user.id }
                            if (index != -1) {
                                users[index] = user.copy(reported = !user.reported)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
@Composable
fun UserCard(user: User, onReportToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDCDCDC) // Gray background
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Column 1: Emoji
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
                    Text(text = user.emoji, style = MaterialTheme.typography.headlineMedium)
                }
            }

            // Column 2: Name & Phone
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

            // Column 3: Email & Report
            Column(
                modifier = Modifier.weight(2f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (user.reported) "REPORTED" else "NOT-REPORTED",
                    color = if (user.reported) Color.Red else Color.Green,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = { /* Edit */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.DarkGray
                    )
                }
                IconButton(
                    onClick = onReportToggle,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = if (user.reported) Color.Red else Color.DarkGray
                    )
                }
            }
        }
    }
}
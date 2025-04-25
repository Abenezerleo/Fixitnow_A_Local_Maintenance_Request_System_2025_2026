package com.example.assigment.ui.theme.others

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.assigment.ui.theme.pages.User
import kotlin.random.Random

@Composable
fun AddUserScreen(
    navController: NavController,
    onAddUser: (User) -> Unit // Callback to add new user
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("👨") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Add New User",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Select emoji:", modifier = Modifier.padding(end = 8.dp))
            IconButton(onClick = { emoji = if (emoji == "👨") "👩" else "👨" }) {
                Text(emoji, style = MaterialTheme.typography.headlineMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val newUser = User(
                    id = Random.nextInt(1000, 9999), // Generate random ID
                    name = name,
                    email = email,
                    phone = phone,
                    emoji = emoji,
                    reported = false
                )
                onAddUser(newUser)
                navController.popBackStack() // Go back to user screen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
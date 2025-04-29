package com.example.assigment.ui.theme.others

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assigment.ui.theme.Entity.User
import com.example.assigment.ui.theme.Enum.UserType
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(onSave: (User) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf<UserType>(UserType.Male) }
    var userTypeExpanded by remember { mutableStateOf(false) }

    val isEmailValid = email.contains("@gmail.com", ignoreCase = true)
    val emailError = if (!isEmailValid) "Email must contain @gmail.com" else null

    val isPhoneValid = phone.length <= 10
    val phoneError = if (!isPhoneValid) "Phone number must not exceed 10 characters" else null

    val isFormValid = isEmailValid && isPhoneValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add User", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = emailError != null,
            supportingText = { if (emailError != null) Text(emailError, color = Color.Red) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            isError = phoneError != null,
            supportingText = { if (phoneError != null) Text(phoneError, color = Color.Red) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = userTypeExpanded,
            onExpandedChange = { userTypeExpanded = !userTypeExpanded }
        ) {
            TextField(
                value = userType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("User Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userTypeExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = userTypeExpanded,
                onDismissRequest = { userTypeExpanded = false }
            ) {
                UserType.values().forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            userType = type
                            userTypeExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = {
                    onSave(
                        User(
                            name = name,
                            email = email,
                            password = password,
                            phoneNumber = phone,
                            userType = userType,
                            DoB = Date(),
                            reported = "No",
                            profileImageUrl = null
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)),
                enabled = isFormValid
            ) {
                Text("Save", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000))
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    }
}

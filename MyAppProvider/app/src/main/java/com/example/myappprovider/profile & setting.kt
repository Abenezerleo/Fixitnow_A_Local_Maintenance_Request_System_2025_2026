package com.example.myappprovider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAndSettingScreen() {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var showSaveButton by remember { mutableStateOf(false) }

    // Personal Info fields
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }

    // Payment fields
    var cbe by remember { mutableStateOf("") }
    var teleBirr by remember { mutableStateOf("") }
    var awashBank by remember { mutableStateOf("") }
    var paypal by remember { mutableStateOf("") }

    // Status field
    var status by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Personal Info Card
            CardSection(title = "Personal Info", isEditing = isEditing, onEditClick = {
                isEditing = true
                showSaveButton = true
            }) {
                EditField("Full Name", fullName, isEditing) { fullName = it }
                EditField("Phone", phone, isEditing) { phone = it }
                EditField("Email", email, isEditing) { email = it }
                EditField("DOB", dob, isEditing) { dob = it }
                EditField("Job Title", jobTitle, isEditing) { jobTitle = it }
            }

            // Payment Details Card
            CardSection(title = "Payment Details", isEditing = isEditing, onEditClick = {
                isEditing = true
                showSaveButton = true
            }) {
                EditField("CBE", cbe, isEditing) { cbe = it }
                EditField("TeleBirr", teleBirr, isEditing) { teleBirr = it }
                EditField("Awash Bank", awashBank, isEditing) { awashBank = it }
                EditField("PayPal", paypal, isEditing) { paypal = it }
            }

            // Status Card
            CardSection(title = "Status", isEditing = isEditing, onEditClick = {
                isEditing = true
                showSaveButton = true
            }) {
                EditField("Status", status, isEditing) { status = it }
            }

            if (showSaveButton) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isEditing = false
                        showSaveButton = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Saved successfully!")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Composable
fun CardSection(
    title: String,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = title, fontSize = 18.sp, color = Color.Black)
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun EditField(label: String, value: String, editable: Boolean, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Black) },
        readOnly = !editable,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textStyle = TextStyle(color = Color.Black)
    )
}

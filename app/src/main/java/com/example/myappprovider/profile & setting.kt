package com.example.myappprovider

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappprovider.viewmodel.ProfileViewModel

@Composable
fun ProfileAndSettingScreen() {
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.Factory(
            (context.applicationContext as MyApplication).providerRepository,
            1L // TODO: Replace with actual provider ID
        )
    )
    
    val provider by viewModel.provider.collectAsState()
    val error by viewModel.error.collectAsState()

    var isEditingPersonalInfo by remember { mutableStateOf(false) }
    var isEditingPaymentInfo by remember { mutableStateOf(false) }
    var isEditingStatus by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf(provider?.fullName ?: "") }
    var phone by remember { mutableStateOf(provider?.phone ?: "") }
    var email by remember { mutableStateOf(provider?.email ?: "") }
    var dateOfBirth by remember { mutableStateOf(provider?.dateOfBirth ?: "") }
    var jobTitle by remember { mutableStateOf(provider?.jobTitle ?: "") }
    var bankName by remember { mutableStateOf(provider?.bankName ?: "") }
    var accountNumber by remember { mutableStateOf(provider?.accountNumber ?: "") }
    var accountHolderName by remember { mutableStateOf(provider?.accountHolderName ?: "") }
    var isAvailable by remember { mutableStateOf(provider?.isAvailable ?: false) }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Profile & Settings", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            // Profile Image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, Color.Gray, CircleShape)
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Info Section
            CardSection(
                title = "Personal Info",
                isEditing = isEditingPersonalInfo,
                onEditClick = { isEditingPersonalInfo = !isEditingPersonalInfo }
            ) {
                EditField(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { fullName = it },
                    enabled = isEditingPersonalInfo
                )
                EditField(
                    label = "Phone",
                    value = phone,
                    onValueChange = { phone = it },
                    enabled = isEditingPersonalInfo
                )
                EditField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    enabled = isEditingPersonalInfo
                )
                EditField(
                    label = "Date of Birth",
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    enabled = isEditingPersonalInfo
                )
                EditField(
                    label = "Job Title",
                    value = jobTitle,
                    onValueChange = { jobTitle = it },
                    enabled = isEditingPersonalInfo
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Details Section
            CardSection(
                title = "Payment Details",
                isEditing = isEditingPaymentInfo,
                onEditClick = { isEditingPaymentInfo = !isEditingPaymentInfo }
            ) {
                EditField(
                    label = "Bank Name",
                    value = bankName,
                    onValueChange = { bankName = it },
                    enabled = isEditingPaymentInfo
                )
                EditField(
                    label = "Account Number",
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    enabled = isEditingPaymentInfo
                )
                EditField(
                    label = "Account Holder Name",
                    value = accountHolderName,
                    onValueChange = { accountHolderName = it },
                    enabled = isEditingPaymentInfo
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Section
            CardSection(
                title = "Status",
                isEditing = isEditingStatus,
                onEditClick = { isEditingStatus = !isEditingStatus }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Available for Work",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    Switch(
                        checked = isAvailable,
                        onCheckedChange = { isAvailable = it },
                        enabled = isEditingStatus
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.updateProfile(
                        fullName = fullName,
                        phone = phone,
                        email = email,
                        dateOfBirth = dateOfBirth,
                        jobTitle = jobTitle,
                        bankName = bankName,
                        accountNumber = accountNumber,
                        accountHolderName = accountHolderName,
                        isAvailable = isAvailable
                    )
                    isEditingPersonalInfo = false
                    isEditingPaymentInfo = false
                    isEditingStatus = false
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Save Changes", fontSize = 16.sp)
            }

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CardSection(
    title: String,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = if (isEditing) "Save" else "Edit",
                        tint = if (isEditing) Color(0xFF4CAF50) else Color.Gray
                    )
                }
            }
            content()
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF4CAF50),
                unfocusedBorderColor = Color.Gray
            )
        )
    }
} 
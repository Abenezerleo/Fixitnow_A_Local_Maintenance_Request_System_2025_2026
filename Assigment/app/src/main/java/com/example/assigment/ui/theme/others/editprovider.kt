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
import com.example.assigment.ui.theme.Entity.ServiceProvider
import com.example.assigment.ui.theme.Enum.ServiceType
import com.example.assigment.ui.theme.Enum.StatusType
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProviderScreen(provider: ServiceProvider, onSave: (ServiceProvider) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf(provider.name ?: "") }
    var email by remember { mutableStateOf(provider.email ?: "") }
    var phone by remember { mutableStateOf(provider.phoneNumber ?: "") }
    var selectedGender by remember { mutableStateOf(provider.gender) }
    var selectedServiceType by remember { mutableStateOf(provider.serviceType) }
    
    // Dropdown states
    var genderExpanded by remember { mutableStateOf(false) }
    var serviceTypeExpanded by remember { mutableStateOf(false) }

    // Options
    val genderOptions = listOf("Male", "Female")
    val serviceTypeOptions = ServiceType.values().toList()

    // Validation
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com", ignoreCase = true)
    val emailError = if (!isEmailValid) "Email must be a valid @gmail.com address" else null
    val isPhoneValid = phone.length == 10 && phone.all { it.isDigit() }
    val phoneError = if (!isPhoneValid) "Phone number must be exactly 10 digits" else null
    val isFormValid = isEmailValid && isPhoneValid && name.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Edit Provider", fontSize = 28.sp, fontWeight = FontWeight.Bold)

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
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            isError = phoneError != null,
            supportingText = { if (phoneError != null) Text(phoneError, color = Color.Red) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Gender dropdown
        ExposedDropdownMenuBox(expanded = genderExpanded, onExpandedChange = { genderExpanded = !genderExpanded }) {
            TextField(
                value = selectedGender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = genderExpanded, onDismissRequest = { genderExpanded = false }) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            selectedGender = gender
                            genderExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        // Service type dropdown
        ExposedDropdownMenuBox(expanded = serviceTypeExpanded, onExpandedChange = { serviceTypeExpanded = !serviceTypeExpanded }) {
            TextField(
                value = selectedServiceType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Service Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceTypeExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = serviceTypeExpanded, onDismissRequest = { serviceTypeExpanded = false }) {
                serviceTypeOptions.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            selectedServiceType = type
                            serviceTypeExpanded = false
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
                        provider.copy(
                            name = name,
                            email = email,
                            phoneNumber = phone,
                            gender = selectedGender,
                            serviceType = selectedServiceType
                        )
                    )
                },
                enabled = isFormValid,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
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

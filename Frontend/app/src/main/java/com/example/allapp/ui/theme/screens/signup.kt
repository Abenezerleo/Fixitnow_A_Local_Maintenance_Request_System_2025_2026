package com.example.allapp.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.allapp.Model.enums.RoleType
import com.example.allapp.Model.enums.ServiceType
import com.example.allapp.ViewModel.AccountViewModel
import com.example.allapp.ViewModel.AccountViewModelFactory
import com.example.allapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(context))
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Requester") }
    var serviceType by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var expandedRole by remember { mutableStateOf(false) }
    var expandedServiceType by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val signupSuccess by viewModel.signupSuccess.collectAsState()

    // Validation states
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Email validation
    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

    // Phone validation
    fun validatePhone(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    if (signupSuccess) {
        // Navigate to login after successful signup
        LaunchedEffect(Unit) {
            navController.navigate("login") { popUpTo("signup") { inclusive = true } }
        }
    }

    // Show error message
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFC7DADB))
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("FixitNow", fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_person),
            contentDescription = "User Icon",
            modifier = Modifier.size(64.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Fields
        SignupTextField(value = username, onValueChange = { username = it }, label = "USERNAME")
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            SignupTextField(
                value = email,
                onValueChange = { 
                    email = it
                    emailError = if (!validateEmail(it) && it.isNotEmpty()) {
                        "Invalid email format (example@gmail.com)"
                    } else null
                },
                label = "EMAIL"
            )
            emailError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            SignupTextField(
                value = phone,
                onValueChange = { 
                    phone = it
                    phoneError = if (!validatePhone(it) && it.isNotEmpty()) {
                        "Phone must be 10 digits"
                    } else null
                },
                label = "PHONE NO."
            )
            phoneError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        SignupTextField(value = password, onValueChange = { password = it }, label = "PASSWORD", isPassword = true)
        Spacer(modifier = Modifier.height(12.dp))
        SignupTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "CONFIRM PASSWORD", isPassword = true)
        Spacer(modifier = Modifier.height(12.dp))
        // Role Dropdown
        Box(modifier = Modifier.fillMaxWidth(0.85f)) {
            ExposedDropdownMenuBox(expanded = expandedRole, onExpandedChange = { expandedRole = !expandedRole }) {
                OutlinedTextField(
                    value = role,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("ROLE") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(expanded = expandedRole, onDismissRequest = { expandedRole = false }) {
                    listOf("Requester", "Provider", "Admin").forEach { selection ->
                        DropdownMenuItem(
                            text = { Text(selection) },
                            onClick = {
                                role = selection
                                expandedRole = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Service Type Dropdown (only shown for Provider role)
        if (role == "Provider") {
            Box(modifier = Modifier.fillMaxWidth(0.85f)) {
                ExposedDropdownMenuBox(expanded = expandedServiceType, onExpandedChange = { expandedServiceType = !expandedServiceType }) {
                    OutlinedTextField(
                        value = serviceType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("SERVICE TYPE") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServiceType) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    ExposedDropdownMenu(expanded = expandedServiceType, onDismissRequest = { expandedServiceType = false }) {
                        ServiceType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.serviceName) },
                                onClick = {
                                    serviceType = type.serviceName
                                    expandedServiceType = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Gender Dropdown
        Box(modifier = Modifier.fillMaxWidth(0.85f)) {
            ExposedDropdownMenuBox(expanded = expandedGender, onExpandedChange = { expandedGender = !expandedGender }) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("GENDER") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                ExposedDropdownMenu(expanded = expandedGender, onDismissRequest = { expandedGender = false }) {
                    listOf("Male", "Female", "Other").forEach { selection ->
                        DropdownMenuItem(
                            text = { Text(selection) },
                            onClick = {
                                gender = selection
                                expandedGender = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Create Account Button
        Button(
            onClick = {
                // Validate all fields before signup
                val isEmailValid = validateEmail(email)
                val isPhoneValid = validatePhone(phone)
                val isPasswordValid = password == confirmPassword
                val isServiceTypeValid = role != "Provider" || serviceType.isNotEmpty()

                if (!isEmailValid) {
                    emailError = "Invalid email format (example@gmail.com)"
                }
                if (!isPhoneValid) {
                    phoneError = "Phone must be 10 digits"
                }
                if (!isPasswordValid) {
                    passwordError = "Passwords do not match"
                }
                if (!isServiceTypeValid) {
                    Toast.makeText(context, "Please select a service type", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (isEmailValid && isPhoneValid && isPasswordValid && isServiceTypeValid) {
                    viewModel.signup(
                        name = username,
                        email = email,
                        phone = phone,
                        password = password,
                        role = when (role) {
                            "Requester" -> RoleType.REQUESTER
                            "Provider" -> RoleType.PROVIDER
                            "Admin" -> RoleType.ADMIN
                            else -> RoleType.REQUESTER
                        },
                        gender = gender,
                        serviceType = if (role == "Provider") serviceType else null
                    )
                } else {
                    Toast.makeText(context, "Please fix the validation errors", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7ED957)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(44.dp)
        ) {
            Text("Create account", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Go Back button
        Button(
            onClick = { navController.navigate("welcome") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB4D8F8)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(44.dp)
        ) {
            Text("Go Back", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupTextField(value: String, onValueChange: (String) -> Unit, label: String, isPassword: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFFE0E0E0),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
} 
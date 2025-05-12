package com.example.allapp.ui.theme.screens
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.allapp.ViewModel.AccountViewModel
import com.example.allapp.ViewModel.AccountViewModelFactory
import com.example.allapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(context))
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState() // Changed to collectAsState
    val error by viewModel.error.collectAsState() // Changed to collectAsState
    val currentUser by viewModel.currentUser.collectAsState() // Changed to collectAsState

    // Handle successful login and role-based navigation
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            println("Current user: $user, role=${user.role}")
            when (user.role.uppercase()) {
                "ADMIN" -> {
                    println("Navigating to admin")
                    navController.navigate("admin") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "REQUESTER" -> {
                    println("Navigating to requester")
                    navController.navigate("requester") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                "PROVIDER" -> {
                    println("Navigating to provider")
                    navController.navigate("provider") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                else -> {
                    println("Unknown role: ${user.role}")
                    Toast.makeText(context, "Unknown role: ${user.role}", Toast.LENGTH_LONG).show()
                }
            }
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
            .background(Color(0xFFF0F0F0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Login page",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp, top = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFC7DADB))
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("FixItNow", fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_person),
            contentDescription = "User Icon",
            modifier = Modifier.size(64.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("USERNAME") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("PASSWORD") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(username, password)
                } else {
                    Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7ED957)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(44.dp)
        ) {
            Text("Login", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("signup") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB4D8F8)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(44.dp)
        ) {
            Text("Create account", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
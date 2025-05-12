package com.example.allapp.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Us") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Get in Touch",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            ContactItem(
                icon = Icons.Default.Email,
                title = "Email",
                content = "support@fixitnow.com"
            )

            ContactItem(
                icon = Icons.Default.Phone,
                title = "Phone",
                content = "+1 (555) 123-4567"
            )

            ContactItem(
                icon = Icons.Default.LocationOn,
                title = "Address",
                content = "123 Main Street, City, Country"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Business Hours",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Monday - Friday: 9:00 AM - 6:00 PM\nSaturday: 10:00 AM - 4:00 PM\nSunday: Closed",
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
            Text(text = content)
        }
    }
} 
package com.example.myappuser.ui.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PersonalInfoCard()
        }
        item {
            RequestHistoryCard()
        }
        item {
            PaymentMethodsCard()
        }
    }
}

@Composable
fun PersonalInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0) // Light gray background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Personal Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Name
            InfoRow("Name", "John Doe")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Phone
            InfoRow("Phone", "+251 912 345 678")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Email
            InfoRow("Email", "john.doe@example.com")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Date of Birth
            InfoRow("Date of Birth", "01/01/1990")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(8.dp)
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFD0D0D0), RoundedCornerShape(12.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = { /* Handle edit */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit $label",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun TableCell(text: String, isStatus: Boolean = false) {
    Box(
        modifier = Modifier
            .width(80.dp)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isStatus) {
                when (text) {
                    "Completed" -> Color(0xFF008000) // Green
                    "In Progress" -> Color(0xFF0000FF) // Blue
                    "Pending" -> Color(0xFFFFA500) // Orange
                    else -> Color.Black
                }
            } else Color.Black
        )
    }
}

@Composable
fun RequestHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Request History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Table Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFD0D0D0), RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    TableCell("Date")
                    TableCell("Request")
                    TableCell("Status")
                    TableCell("Provider")
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.Black)
                        .align(Alignment.BottomCenter)
                )
            }

            // Sample data rows
            val requests = listOf(
                RequestHistory("2024-04-30", "Plumbing repair", "Completed", "John's Plumbing"),
                RequestHistory("2024-04-29", "Electrical work", "In Progress", "Electric Solutions"),
                RequestHistory("2024-04-28", "AC maintenance", "Pending", "Cool Air Services")
            )

            requests.forEachIndexed { index, request ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (index % 2 == 0) Color(0xFFE0E0E0) else Color(0xFFD8D8D8),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    TableCell(request.date)
                    TableCell(request.description)
                    TableCell(request.status, isStatus = true)
                    TableCell(request.provider)
                }
            }
        }
    }
}

@Composable
fun PaymentMethodsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Payment Methods",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // CBE
            InfoRow("CBE", "1234 5678 9012 3456")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // TeleBirr
            InfoRow("TeleBirr", "+251 912 345 678")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Awash
            InfoRow("Awash", "1234 5678 9012 3456")
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // PayPal
            InfoRow("PayPal", "john.doe@example.com")
        }
    }
}

data class RequestHistory(
    val date: String,
    val description: String,
    val status: String,
    val provider: String
)
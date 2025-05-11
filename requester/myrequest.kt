package com.example.allapp.ui.theme.requester

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class RequestStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    REJECTED
}

@Composable
fun MyRequestScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        Text(
            "My Requests",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Sample data - replace with your actual data source
        val requests = listOf(
            RequestData(
                description = "Plumbing repair needed",
                date = "2024-04-30",
                providerName = "John's Plumbing",
                providerNumber = "+1234567890",
                status = RequestStatus.PENDING
            ),
            RequestData(
                description = "Electrical wiring issue",
                date = "2024-04-29",
                providerName = "Electric Solutions",
                providerNumber = "+0987654321",
                status = RequestStatus.IN_PROGRESS
            ),
            RequestData(
                description = "AC maintenance",
                date = "2024-04-28",
                providerName = "Cool Air Services",
                providerNumber = "+1122334455",
                status = RequestStatus.COMPLETED
            )
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(requests) { request ->
                MyRequestCard(
                    description = request.description,
                    date = request.date,
                    providerName = request.providerName,
                    providerNumber = request.providerNumber,
                    status = request.status
                )
            }
        }
    }
}

@Composable
fun MyRequestCard(
    description: String,
    date: String,
    providerName: String,
    providerNumber: String,
    status: RequestStatus,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0) // Light gray background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top row with description and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                StatusText(status)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Request details
            Text(
                text = "Date: $date",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Provider: $providerName",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Contact: $providerNumber",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Handle mark as complete */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF90EE90) // Light green
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text(
                        "Mark as Complete",
                        color = Color.Black
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = { /* Handle decline */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFB6C1) // Light red
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text(
                        "Decline",
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun StatusText(status: RequestStatus) {
    val (backgroundColor, text) = when (status) {
        RequestStatus.PENDING -> Color(0xFF808080) to "Pending" // Gray
        RequestStatus.IN_PROGRESS -> Color(0xFF0000FF) to "In Progress" // Blue
        RequestStatus.COMPLETED -> Color(0xFF008000) to "Completed" // Green
        RequestStatus.REJECTED -> Color(0xFFFF0000) to "Rejected" // Red
    }

    Card(
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

data class RequestData(
    val description: String,
    val date: String,
    val providerName: String,
    val providerNumber: String,
    val status: RequestStatus
)
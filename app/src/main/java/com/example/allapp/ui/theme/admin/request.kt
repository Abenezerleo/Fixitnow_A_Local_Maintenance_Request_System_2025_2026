package com.example.allapp.ui.theme.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Request(
    val Typeemoji: String,
    val Type: String,
    val Id: String,
    val provider: String,
    val Time: String,
    val Date: String,
    val completed: Boolean,
    val reported: Boolean
)

@Composable
fun RequestScreen() {
    val requestList = listOf(
        Request("🔧", "Plumbing", "REQ-001", "John Doe", "10:00 AM", "2025-04-22", false, false),
        Request("🧹", "Cleaning", "REQ-002", "Alice Smith", "11:30 AM", "2025-04-21", true, false),
        Request("💡", "Electrical", "REQ-003", "Bob Electric", "2:00 PM", "2025-04-20", false, true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Manage Request",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val buttonShape = RoundedCornerShape(12.dp)
            val commonModifier = Modifier
                .weight(1f)
                .height(48.dp)
                .border(1.dp, Color.Black, buttonShape)

            listOf("ALL", "Completed", "Rejected").forEach { label ->
                Button(
                    onClick = { /* Handle click */ },
                    modifier = commonModifier,
                    shape = buttonShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(label, color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Request List", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)

        requestList.forEach { request ->
            RequestCard(request = request)
        }
    }
}

@Composable
fun RequestCard(request: Request) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = request.Typeemoji, style = MaterialTheme.typography.headlineMedium)
            }
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = request.Type,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = request.Id, color = Color.Gray)
            Text(text = request.provider, color = Color.Gray)
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp)
        ) {
            Text("Request Time")
            Text(text = request.Time, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = request.Date,
                color = if (request.reported ) Color.Red else Color.Green,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = { /* Handle status change */ },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (request.completed) Icons.Default.Done else Icons.Default.Clear,
                    contentDescription = if (request.completed) "Completed" else "Pending",
                    tint = Color.DarkGray
                )
            }
        }
    }
}

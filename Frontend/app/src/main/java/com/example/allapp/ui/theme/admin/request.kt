package com.example.allapp.ui.theme.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.allapp.Model.Request
import com.example.allapp.Model.RequestStatus
import com.example.allapp.Model.ServiceType
import com.example.allapp.ViewModel.RequestViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RequestScreen(viewModel: RequestViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val requests by viewModel.requests.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val error by viewModel.error.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    var selectedFilter by remember { mutableStateOf("ALL") }

    // Show error toast
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

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
                    onClick = {
                        selectedFilter = label
                        when (label) {
                            "ALL" -> viewModel.fetchAllRequests()
                            "Completed" -> viewModel.fetchCompletedRequests()
                            "Rejected" -> viewModel.fetchRejectedRequests()
                        }
                    },
                    modifier = commonModifier,
                    shape = buttonShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFilter == label) Color(0xFF006400) else Color.White,
                        contentColor = if (selectedFilter == label) Color.White else Color.Black
                    )
                ) {
                    Text(label)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Request List", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text(
                text = error ?: "An error occurred",
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (requests.isEmpty()) {
            Text(
                text = "No requests found",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn {
                items(requests) { request ->
                    RequestCard(
                        request = request,
                        onStatusToggle = { newStatus ->
                            viewModel.updateRequestStatus(request.requestId, newStatus)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun RequestCard(
    request: Request,
    onStatusToggle: (RequestStatus) -> Unit
) {
    // Map serviceType to emoji with null safety
    val typeEmoji = when (request.serviceType) {
        com.example.allapp.Model.ServiceType.CLEANING -> "🧹"
        com.example.allapp.Model.ServiceType.PLUMBING -> "🔧"
        com.example.allapp.Model.ServiceType.ELECTRICAL -> "💡"
        com.example.allapp.Model.ServiceType.CARPENTRY -> "🪚"
        com.example.allapp.Model.ServiceType.PAINTING -> "🎨"
        com.example.allapp.Model.ServiceType.GARDENING -> "🌱"
        com.example.allapp.Model.ServiceType.MOVING -> "📦"
        com.example.allapp.Model.ServiceType.OTHER -> "📋"
        null -> "❓" // Default emoji for null serviceType
    }

    // Format date and time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val displayDate = request.scheduledDate?.let { dateFormat.format(it) } ?: dateFormat.format(request.createdAt)
    val displayTime = request.scheduledDate?.let { timeFormat.format(it) } ?: timeFormat.format(request.createdAt)

    // Determine status color
    val statusColor = when (request.status) {
        RequestStatus.COMPLETED -> Color.Green
        RequestStatus.REJECTED -> Color.Red
        RequestStatus.CANCELLED -> Color.Red
        RequestStatus.PENDING -> Color.Gray
        RequestStatus.ACCEPTED -> Color.Blue
        RequestStatus.IN_PROGRESS -> Color(0xFFFFA500) // Orange color for in progress
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
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
                Text(text = typeEmoji, style = MaterialTheme.typography.headlineMedium)
            }
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = request.serviceType?.name ?: "Unknown Service",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ID: ${request.requestId}", color = Color.Gray)
            Text(text = request.provider?.name ?: "Unassigned", color = Color.Gray)
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(end = 8.dp)
        ) {
            Text("Request Time")
            Text(text = displayTime, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = displayDate,
                color = statusColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = {
                    // Toggle between PENDING, COMPLETED, REJECTED
                    val newStatus = when (request.status) {
                        RequestStatus.PENDING -> RequestStatus.COMPLETED
                        RequestStatus.COMPLETED -> RequestStatus.REJECTED
                        RequestStatus.REJECTED -> RequestStatus.PENDING
                        RequestStatus.ACCEPTED -> RequestStatus.COMPLETED
                        RequestStatus.CANCELLED -> RequestStatus.PENDING
                        RequestStatus.IN_PROGRESS-> RequestStatus.IN_PROGRESS
                    }
                    onStatusToggle(newStatus)
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = when (request.status) {
                        RequestStatus.COMPLETED -> Icons.Default.Done
                        RequestStatus.REJECTED -> Icons.Default.Clear
                        else -> Icons.Default.Clear
                    },
                    contentDescription = request.status.toString(),
                    tint = statusColor
                )
            }
        }
    }
}

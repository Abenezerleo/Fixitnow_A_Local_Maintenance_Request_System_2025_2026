package com.example.assigment.ui.theme.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assigment.ui.theme.Entity.Request
import com.example.assigment.ui.theme.ViewModel.RequestViewModel
import kotlinx.coroutines.flow.combine

sealed class RequestFilter {
    object All : RequestFilter()
    object Completed : RequestFilter()
    object Rejected : RequestFilter()
}

@Composable
fun RequestScreen() {
    val viewModel: RequestViewModel = viewModel()
    val allRequests by viewModel.allRequests.collectAsState(initial = emptyList())
    val completedRequests by viewModel.getRequestsByStatus(true).collectAsState(initial = emptyList())
    val rejectedRequests by viewModel.getRequestsByRejectedStatus(true).collectAsState(initial = emptyList())
    
    var selectedFilter by remember { mutableStateOf<RequestFilter>(RequestFilter.All) }
    
    val filteredRequests = remember(selectedFilter, allRequests, completedRequests, rejectedRequests) {
        when (selectedFilter) {
            is RequestFilter.All -> allRequests
            is RequestFilter.Completed -> completedRequests
            is RequestFilter.Rejected -> rejectedRequests
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

            listOf(
                Pair("ALL", RequestFilter.All),
                Pair("Completed", RequestFilter.Completed),
                Pair("Rejected", RequestFilter.Rejected)
            ).forEach { (label, filter) ->
                Button(
                    onClick = { selectedFilter = filter },
                    modifier = commonModifier,
                    shape = buttonShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFilter == filter) Color(0xFF006400) else Color.White
                    )
                ) {
                    Text(
                        label,
                        color = if (selectedFilter == filter) Color.White else Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Request List (${filteredRequests.size})",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        filteredRequests.forEach { request ->
            RequestCard(
                request = request,
                onStatusChange = { updatedRequest ->
                    if (updatedRequest.completed) {
                        viewModel.completeRequest(updatedRequest)
                    } else if (updatedRequest.rejected) {
                        viewModel.rejectRequest(updatedRequest)
                    } else {
                        viewModel.updateRequest(updatedRequest)
                    }
                }
            )
        }
    }
}

@Composable
fun RequestCard(request: Request, onStatusChange: (Request) -> Unit) {
    Column {
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
                        .background(
                            when {
                                request.rejected -> Color(0xFFFFE5E5)
                                request.completed -> Color(0xFFE5FFE5)
                                else -> Color(0xFFFFF7E5)
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = request.typeEmoji,
                        style = MaterialTheme.typography.headlineMedium,
                        color = when {
                            request.rejected -> Color(0xFFFF0000)
                            request.completed -> Color(0xFF00AA00)
                            else -> Color(0xFFFFA500)
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = request.type,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = request.requestId, color = Color.Gray)
                Text(text = request.providerName, color = Color.Gray)
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(end = 8.dp)
            ) {
                Text("Request Time")
                Text(text = request.time, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = request.date,
                    color = when {
                        request.rejected -> Color(0xFFFF0000)
                        request.completed -> Color(0xFF00AA00)
                        else -> Color(0xFFFFA500)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!request.completed && !request.rejected) {
                // Show nothing for pending requests
            } else {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { 
                            if (request.rejected) {
                                onStatusChange(request.copy(rejected = false))
                            } else if (request.completed) {
                                onStatusChange(request.copy(completed = false))
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = when {
                                request.rejected -> Icons.Default.Clear
                                request.completed -> Icons.Default.Done
                                else -> Icons.Default.Clear
                            },
                            contentDescription = when {
                                request.rejected -> "Rejected"
                                request.completed -> "Completed"
                                else -> "Pending"
                            },
                            tint = when {
                                request.rejected -> Color(0xFFFF0000)
                                request.completed -> Color(0xFF00AA00)
                                else -> Color(0xFFFFA500)
                            }
                        )
                    }
                }
            }
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

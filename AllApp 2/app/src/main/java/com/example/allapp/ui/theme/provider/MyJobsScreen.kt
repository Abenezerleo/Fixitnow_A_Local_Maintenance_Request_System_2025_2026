package com.example.allapp.ui.theme.provider

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.allapp.Model.Request
import com.example.allapp.Model.RequestStatus
import com.example.allapp.ViewModel.JobRequestViewModel
import com.example.allapp.ViewModel.JobRequestViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyJobsScreen(viewModel: JobRequestViewModel = viewModel(
    factory = JobRequestViewModelFactory(LocalContext.current.applicationContext as Application)
)) {
    var showCompleted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Fetch requests based on the selected tab
    LaunchedEffect(showCompleted) {
        if (showCompleted) {
            viewModel.fetchProviderCompletedRequests()
        } else {
            viewModel.fetchProviderAcceptedRequests()
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                text = "My jobs",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0)),
            ) {
                val tabModifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showCompleted = false }
                    .background(if (!showCompleted) Color(0xFF8EDC74) else Color(0xFFE0E0E0))
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)

                val tabModifier2 = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { showCompleted = true }
                    .background(if (showCompleted) Color(0xFF8EDC74) else Color(0xFFE0E0E0))
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp)

                Box(tabModifier, contentAlignment = Alignment.Center) {
                    Text("Active", fontWeight = FontWeight.Bold, color = Color.Black)
                }

                Box(tabModifier2, contentAlignment = Alignment.Center) {
                    Text("Completed", fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.isLoading.collectAsState().value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val requests = if (showCompleted) {
                    viewModel.providerCompletedRequests.collectAsState().value
                } else {
                    viewModel.providerAcceptedRequests.collectAsState().value
                }

                if (requests.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showCompleted) "No completed jobs" else "No active jobs",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(requests) { request ->
                            JobCard(request = request)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobCard(request: Request) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = request.serviceType.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = request.description, fontSize = 14.sp, color = Color.Black)
            Text(text = request.requester.name, fontSize = 14.sp, color = Color.Black)
            Text(text = request.requester.phone, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                request.scheduledDate?.let { date ->
                    Column {
                        Text(
                            text = timeFormat.format(date),
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = dateFormat.format(date),
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                Text(
                    text = request.status.name,
                    fontSize = 14.sp,
                    color = when (request.status) {
                        RequestStatus.COMPLETED -> Color(0xFF4CAF50)
                        RequestStatus.IN_PROGRESS -> Color(0xFFFFA500)
                        else -> Color.Black
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
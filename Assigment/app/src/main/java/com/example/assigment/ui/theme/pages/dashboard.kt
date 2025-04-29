package com.example.assigment.ui.theme.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assigment.ui.theme.ViewModel.DashboardViewModel
import com.example.assigment.ui.theme.others.PieChartScreen
import android.util.Log

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = viewModel()
    
    // Collect all the counts from the ViewModel
    val totalUsers by viewModel.totalUserCount.collectAsState(initial = 0)
    val activeUsers by viewModel.activeUserCount.collectAsState(initial = 0)
    val availableProviders by viewModel.availableProviderCount.collectAsState(initial = 0)
    val totalRequests by viewModel.totalRequestCount.collectAsState(initial = 0)
    val completedRequests by viewModel.completedRequestCount.collectAsState(initial = 0)
    val pendingRequests by viewModel.pendingRequestCount.collectAsState(initial = 0)
    val rejectedRequests by viewModel.rejectedRequestCount.collectAsState(initial = 0)

    // Debug logging
    LaunchedEffect(completedRequests, pendingRequests, rejectedRequests) {
        Log.d("DashboardScreen", "Completed: $completedRequests")
        Log.d("DashboardScreen", "Pending: $pendingRequests")
        Log.d("DashboardScreen", "Rejected: $rejectedRequests")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F9FA))
            .padding(16.dp)
    ) {
        Text(
            text = "Dashboard",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Top Stats Cards",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // First row of metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricCard(title = "Total Users", value = totalUsers.toString())
            MetricCard(title = "Pending Requests", value = pendingRequests.toString())
        }

        // Second row of metrics
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricCard(title = "Available Providers", value = availableProviders.toString())
            MetricCard(title = "Completed Requests", value = completedRequests.toString())
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        // Update PieChart with real data
        PieChartScreen(
            pendingCount = pendingRequests,
            completedCount = completedRequests,
            rejectedCount = rejectedRequests
        )
    }
}

@Composable
fun MetricCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
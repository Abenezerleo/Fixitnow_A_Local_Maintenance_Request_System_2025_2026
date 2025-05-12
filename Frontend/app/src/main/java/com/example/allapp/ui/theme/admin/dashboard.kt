package com.example.allapp.ui.theme.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.allapp.ViewModel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val totalRequesters by viewModel.totalRequesters.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val totalProviders by viewModel.totalProviders.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val totalCompleted by viewModel.totalCompleted.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val totalPending by viewModel.totalPending.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)
    val error by viewModel.error.collectAsStateWithLifecycle(lifecycleOwner = lifecycleOwner)

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
            text = "Top Chart Cards",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color(0xFF2196F3)
                )
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error ?: "An error occurred",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First row of metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MetricCard(title = "Total Users", value = totalRequesters.toString())
                    MetricCard(title = "Pending Requests", value = totalPending.toString())
                }

                // Second row of metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MetricCard(title = "Total Providers", value = totalProviders.toString())
                    MetricCard(title = "Completed Requests", value = totalCompleted.toString())
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .height(120.dp)
            .width(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
package com.example.myappprovider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappprovider.viewmodel.PerformanceViewModel

@Composable
fun PerformanceOverviewScreen() {
    val context = LocalContext.current
    val viewModel: PerformanceViewModel = viewModel(
        factory = PerformanceViewModel.Factory(
            (context.applicationContext as MyApplication).providerRepository,
            1L // TODO: Replace with actual provider ID
        )
    )
    
    val provider by viewModel.provider.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Performance Overview",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Jobs", color = Color.Black)
                        Text(
                            provider?.totalJobs?.toString() ?: "0",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Completed Jobs", color = Color.Black)
                        Text(
                            provider?.completedJobs?.toString() ?: "0",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Average Rating", color = Color.Black)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val starColor = Color(0xFFFFD700) // Yellow (gold) color
                            repeat(4) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Star",
                                    tint = starColor
                                )
                            }
                            Text(
                                provider?.rating?.toString() ?: "0.0",
                                color = Color.Black,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
} 
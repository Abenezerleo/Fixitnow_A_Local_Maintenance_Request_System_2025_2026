package com.example.allapp.ui.theme.provider

import android.app.Application
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.allapp.ViewModel.JobRequestViewModel
import com.example.allapp.ViewModel.JobRequestViewModelFactory
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: JobRequestViewModel = viewModel(
    factory = JobRequestViewModelFactory(LocalContext.current.applicationContext as Application)
)) {
    val context = LocalContext.current
    val completedCount by viewModel.providerCompletedCount.collectAsState()
    val averageRating by viewModel.providerAverageRating.collectAsState()
    val totalBudget by viewModel.providerTotalBudget.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Fetch statistics when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchProviderStats()
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                "Performance Overview",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "An error occurred",
                        color = Color.Red
                    )
                }
            } else {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFC0C0C0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Earning", color = Color.Black)
                            Text(
                                "${NumberFormat.getCurrencyInstance(Locale("en", "ET")).format(totalBudget)}",
                                color = Color.Black,
                                modifier = Modifier
                                    .background(Color(0xFFD0D0D0), shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Task Completed", color = Color.Black)
                            Text(
                                completedCount.toString(),
                                color = Color.Black,
                                modifier = Modifier
                                    .background(Color(0xFFD0D0D0), shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Average Rating", color = Color.Black)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val starColor = Color(0xFFFFD700) // Yellow (gold) color
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Star",
                                        tint = if (index < averageRating.toInt()) starColor else Color.Gray
                                    )
                                }
                                Text(
                                    String.format("%.1f", averageRating),
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

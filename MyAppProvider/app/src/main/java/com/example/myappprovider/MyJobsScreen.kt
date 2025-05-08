package com.example.myappprovider

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyJobsScreen() {
    var showCompleted by remember { mutableStateOf(false) }

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

            val jobs = getJobListings().filter { it.isCompleted == showCompleted }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(jobs) { job ->
                    JobCard(job = job)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun JobCard(job: JobListing) {
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
                text = job.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (!job.isCompleted) {
                LinearProgressIndicator(
                    progress = 0.6f, // Replace with job.progress if available
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color.Blue,
                    trackColor = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = job.clientName, fontSize = 14.sp, color = Color.Black)
            Text(text = job.phoneNumber, fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = job.location,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                if (job.isCompleted) {
                    Text(
                        text = "Completed",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class JobListing(
    val title: String,
    val clientName: String,
    val phoneNumber: String,
    val location: String,
    val isCompleted: Boolean = false
)

fun getJobListings(): List<JobListing> {
    return listOf(
        JobListing(
            title = "Fix leaky faucet",
            clientName = "Blen Gebre",
            phoneNumber = "09272937803",
            location = "Kitchen Sink"
        ),
        JobListing(
            title = "Repair wall outlet",
            clientName = "Ema Abrheam",
            phoneNumber = "09272937803",
            location = "Living Room",
            isCompleted = true
        ),
        JobListing(
            title = "Check Water Heater",
            clientName = "Jon Smith",
            phoneNumber = "092471824693",
            location = "Basement",
            isCompleted = true
        )
    )
}

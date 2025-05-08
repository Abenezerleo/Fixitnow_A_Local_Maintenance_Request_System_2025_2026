package com.example.myappprovider

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myappprovider.viewmodel.JobRequestViewModel

@Composable
fun JobRequestScreen() {
    val context = LocalContext.current
    val viewModel: JobRequestViewModel = viewModel(
        factory = JobRequestViewModel.Factory(
            (context.applicationContext as MyApplication).jobRepository,
            1L // TODO: Replace with actual provider ID
        )
    )
    
    val availableJobs by viewModel.availableJobs.collectAsState()
    val assignedJobs by viewModel.assignedJobs.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Job Requests", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            
            // Available Jobs Section
            Text("Available Jobs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))
            if (availableJobs.isEmpty()) {
                Text("No available jobs", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                availableJobs.forEach { job ->
                    JobItem(
                        title = job.title,
                        location = job.location,
                        wage = job.wage,
                        startTime = job.startTime,
                        endTime = job.endTime,
                        date = job.date,
                        onAccept = { viewModel.acceptJob(job.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Assigned Jobs Section
            Text("Assigned Jobs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))
            if (assignedJobs.isEmpty()) {
                Text("No assigned jobs", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                assignedJobs.forEach { job ->
                    AssignedJobItem(
                        title = job.title,
                        description = job.description,
                        location = job.location,
                        wage = job.wage,
                        startTime = job.startTime,
                        endTime = job.endTime,
                        date = job.date,
                        status = if (job.isCompleted) "Completed" else "In Progress"
                    )
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

@Composable
fun JobItem(
    title: String,
    location: String,
    wage: String,
    startTime: String,
    endTime: String,
    date: String,
    onAccept: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, Color.Gray, CircleShape)
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(location, fontSize = 14.sp, color = Color.Gray)
                Text(wage, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Accept")
                }
            }

            Spacer(modifier = Modifier.width(40.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text("$startTime -", fontSize = 14.sp, color = Color.Black)
                Text(endTime, fontSize = 14.sp, color = Color.Black)
                Text(date, fontSize = 14.sp, color = Color.Black)
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun AssignedJobItem(
    title: String,
    description: String,
    location: String,
    wage: String,
    startTime: String,
    endTime: String,
    date: String,
    status: String
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(1.dp, Color.Gray, CircleShape)
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(description, fontSize = 14.sp, color = Color.Gray)
                Text(location, fontSize = 14.sp, color = Color.Gray)
                Text(wage, fontSize = 14.sp, color = Color.Gray)
                Text("$startTime - $endTime", fontSize = 14.sp, color = Color.Gray)
                Text(date, fontSize = 14.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.width(40.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text("Status:", fontSize = 14.sp, color = Color.Black)
                Text(status, fontSize = 14.sp, color = Color.Black)
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
} 
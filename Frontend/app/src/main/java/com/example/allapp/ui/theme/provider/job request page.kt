package com.example.allapp.ui.theme.provider

import android.app.Application
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.allapp.Model.Request
import com.example.allapp.R
import com.example.allapp.ViewModel.JobRequestViewModel
import com.example.allapp.ViewModel.JobRequestViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Ui(viewModel: JobRequestViewModel = viewModel(
    factory = JobRequestViewModelFactory(LocalContext.current.applicationContext as Application)
)) {
    val unassignedRequests by viewModel.unassignedRequests.collectAsState()
    val providerRequests by viewModel.providerRequests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
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
                Text(
                    text = error ?: "An error occurred",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Text("Available Jobs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

                unassignedRequests.forEach { request ->
                    JobItem(
                        request = request,
                        onAccept = { viewModel.acceptRequest(request.requestId) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("My Jobs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

                providerRequests.forEach { request ->
                    AssignedJobItem(request = request)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
 TopAppBar(
  title = {
   Box(modifier = Modifier.fillMaxWidth()) {
    Text(
     "FixitNow",
     fontSize = 20.sp,
     fontWeight = FontWeight.Bold,
     color = Color.Black,
     modifier = Modifier.align(Alignment.Center)
    )
    Text(
     "Joe Doe",
     fontSize = 14.sp,
     color = Color.Black,
     modifier = Modifier
      .align(Alignment.CenterEnd)
      .padding(top = 6.dp, end = 8.dp)
    )
   }
  },
  navigationIcon = {
   IconButton(onClick = { }) {
    Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.Black)
   }
  },
  colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFADD8E6)) // Light blue top bar
 )
}

@Composable
fun JobItem(
    request: Request,
    onAccept: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

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
                Text(request.serviceType.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(request.description, fontSize = 14.sp, color = Color.Gray)
                Text("${request.budget ?: 0} ETB", fontSize = 14.sp, color = Color.Gray)
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
                request.scheduledDate?.let { date ->
                    Text(timeFormat.format(date), fontSize = 14.sp, color = Color.Black)
                    Text(dateFormat.format(date), fontSize = 14.sp, color = Color.Black)
                }
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun AssignedJobItem(request: Request) {
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
                Text(request.serviceType.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(request.description, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(40.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text("Status:", fontSize = 14.sp, color = Color.Black)
                Text(
                    request.status.name,
                    fontSize = 14.sp,
                    color = when (request.status.name.lowercase()) {
                        "completed" -> Color(0xFF4CAF50)
                        "in_progress" -> Color(0xFFFFA000)
                        else -> Color.Black
                    }
                )
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequesterScreen() {
    Ui()
}

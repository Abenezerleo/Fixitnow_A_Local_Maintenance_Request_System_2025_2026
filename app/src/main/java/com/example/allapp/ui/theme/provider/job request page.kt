package com.example.allapp.ui.theme.provider

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allapp.R

@Composable
fun Ui() {
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
   Text("Available Jobs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

   JobItem("Plumber", "Addis Ababa", "1200 ETB", "9:00 AM", "5:00 PM", "02/01/2025")
   JobItem("Electrician", "Bole", "1500 ETB", "10:00 AM", "6:00 PM", "03/01/2025")

   Spacer(modifier = Modifier.height(24.dp))

   Text("Assigned Jobs", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

   AssignedJobItem("Carpenter", "Making kitchen cabinet", "Completed")
   AssignedJobItem("Painter", "Painting bedroom wall", "In Progress")
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
fun JobItem(title: String, location: String, wage: String, startTime: String, endTime: String, date: String) {
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
     onClick = { },
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
fun AssignedJobItem(title: String, description: String, status: String) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequesterScreen() {
 Ui()
}

package com.example.allapp.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.allapp.R
@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFC7DADB))
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("FixitNow", fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Logo
        Icon(
            painter = painterResource(id = R.drawable.fixitnow),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Header
        Text(
            "\"Need a Pro? Get It Done in Minutes!\"",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        // Subtitle
        Text(
            "Say goodbye to endless searching—our platform connects you with trusted, top-rated service providers instantly! Whether it's plumbing, Painting, or home repairs, just browse, request, and relax. Track progress in real-time, pay securely, and enjoy hassle-free help. Your to-do list just met its match!",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7ED957)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Sign in", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { navController.navigate("signup") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB4D8F8)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Sign up", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
package com.example.allapp.ui.theme.requester

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.allapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentScreen() {
    val backgroundColor = Color(0xFFF8F8F8)
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Search for services...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF8EA1A3),
                    unfocusedBorderColor = Color(0xFFC4D8DA)
                )
            )
        }

        // Trending Services Section
        item {
            Text(
                "Trending Services",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(trendingServices) { service ->
                    TrendingServiceCard(service)
                }
            }
        }

        // Top Providers Section
        item {
            Text(
                "Top Providers",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(topProviders) { provider ->
            TopProviderCard(provider)
        }

        // Metrics Section
        item {
            Text(
                "Service Metrics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        item {
            MetricsCard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingServiceCard(service: TrendingService) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            // Image placeholder with background color
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(service.backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = service.iconResId),
                    contentDescription = service.label,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = service.label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = service.rating.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                Text(
                    text = "${service.price} ETB",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopProviderCard(provider: TopProvider) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = provider.emoji,
                fontSize = 32.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = provider.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = provider.serviceType,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            Text(
                text = "${provider.baseSalary} ETB",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricsCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MetricItem("Total Providers", "150")
        MetricItem("Average Rating", "4.8")
    }
}

@Composable
fun MetricItem(label: String, value: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

data class TrendingService(
    val label: String,
    val rating: Float,
    val price: Double,
    val backgroundColor: Color,
    val iconResId: Int
)

data class TopProvider(
    val emoji: String,
    val name: String,
    val serviceType: String,
    val baseSalary: Int
)

// Sample data
private val trendingServices = listOf(
    TrendingService(
        label = "Plumbing Service",
        rating = 4.8f,
        price = 89.99,
        backgroundColor = Color(0xFF2196F3),
        iconResId = R.drawable.recruitment
    ),
    TrendingService(
        label = "Electrical Repair",
        rating = 4.7f,
        price = 75.50,
        backgroundColor = Color(0xFF4CAF50),
        iconResId = R.drawable.communication
    ),
    TrendingService(
        label = "House Cleaning",
        rating = 4.9f,
        price = 65.00,
        backgroundColor = Color(0xFFFF9800),
        iconResId = R.drawable.request
    )
)

private val topProviders = listOf(
    TopProvider("👨", "John Smith", "Plumbing", 800),
    TopProvider("👩", "Sarah Johnson", "Electrical", 1200),
    TopProvider("👨", "Mike Brown", "Cleaning", 600)
) 
package com.example.assigment.ui.theme.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colors (inline)
val LightGray = Color(0xFFD3D3D3)
val Red = Color(0xFFFF0000)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Green = Color(0xFF006400)

data class Provider(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val emoji: String,
    val reported: Boolean,
    val providerType: String,
    val rating: Float
)

@Composable
fun ProviderScreen() {
    val providers = remember {
        mutableStateListOf(
            Provider(1, "John Doe", "john@example.com", "+123456789", "👨", true, "Plumber", 4.5f),
            Provider(2, "Jane Smith", "jane@example.com", "+987654321", "👩", false, "Electrician", 4.2f),
            Provider(3, "Bob Johnson", "bob@example.com", "+1122334455", "👨", false, "Carpenter", 3.8f)
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Text(
            text = "Providers",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Search Provider...") },
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray.copy(alpha = 0.2f),
                    unfocusedContainerColor = LightGray.copy(alpha = 0.2f)
                )
            )

            Button(
                onClick = { /* Add Provider */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "ADD PROVIDER", color = White)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Available Service Providers",
                color = Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(providers) { provider ->
                    ProviderCard(
                        provider = provider,
                        onReportToggle = {
                            val index = providers.indexOfFirst { it.id == provider.id }
                            if (index != -1) {
                                providers[index] = provider.copy(reported = !provider.reported)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* Edit Provider */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, LightGray),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = White,
                                contentColor = Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit", color = Black)
                        }

                        OutlinedButton(
                            onClick = { /* Delete Provider */ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, LightGray),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = White,
                                contentColor = if (provider.reported) Red else Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = if (provider.reported) Red else Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Delete",
                                color = if (provider.reported) Red else Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ProviderCard(provider: Provider, onReportToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(LightGray.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = provider.emoji, style = MaterialTheme.typography.headlineMedium)
                }
            }

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = provider.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = provider.providerType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightGray
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${provider.rating} Stars",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                )
            }
        }
    }
}

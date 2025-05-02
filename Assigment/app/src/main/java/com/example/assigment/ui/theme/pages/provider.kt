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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assigment.ui.theme.Entity.Account
import com.example.assigment.ui.theme.Enum.ServiceType
import com.example.assigment.ui.theme.Enum.StatusType
import com.example.assigment.ui.theme.ViewModel.ServiceProviderViewModel
import com.example.assigment.ui.theme.others.AddProviderScreen
import com.example.assigment.ui.theme.others.EditProviderScreen
import java.util.*

// Colors (inline)
val LightGray = Color(0xFFD3D3D3)
val Red = Color(0xFFFF0000)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Green = Color(0xFF006400)

sealed class ProviderPageState {
    object List : ProviderPageState()
    object Add : ProviderPageState()
    data class Edit(val provider: Account) : ProviderPageState()
}

@Composable
fun ProviderScreen(viewModel: ServiceProviderViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var providerPageState by remember { mutableStateOf<ProviderPageState>(ProviderPageState.List) }
    val providers by viewModel.providers.collectAsState(initial = emptyList())

    when (providerPageState) {
        is ProviderPageState.List -> {
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
                        onClick = { providerPageState = ProviderPageState.Add },
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
                        items(providers.filter { 
                            it.fullName?.contains(searchQuery, ignoreCase = true) == true ||
                            it.email?.contains(searchQuery, ignoreCase = true) == true
                        }) { provider ->
                            ProviderCard(
                                provider = provider,
                                onEdit = { providerPageState = ProviderPageState.Edit(provider) },
                                onDelete = { viewModel.deleteProvider(provider.accountId) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
        is ProviderPageState.Add -> {
            AddProviderScreen(
                onAddProvider = { fullName, email, phoneNumber, password, gender ->
                    viewModel.addProvider(
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        gender = gender,
                    )
                    providerPageState = ProviderPageState.List
                },
                onCancel = { providerPageState = ProviderPageState.List }
            )
        }
        is ProviderPageState.Edit -> {
            val provider = (providerPageState as ProviderPageState.Edit).provider
            EditProviderScreen(
                provider = provider,
                onUpdateProvider = { fullName, email, phoneNumber, password, gender ->
                    viewModel.updateProvider(
                        accountId = provider.accountId,
                        fullName = fullName,
                        email = email,
                        phoneNumber = phoneNumber,
                        password = password,
                        gender = gender
                    )
                    providerPageState = ProviderPageState.List
                },
                onCancel = { providerPageState = ProviderPageState.List }
            )
        }
    }
}

@Composable
fun ProviderCard(
    provider: Account,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(0.dp),
        border = BorderStroke(2.dp, LightGray),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main row with three columns
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left column - Emoji
                Text(
                    text = if (provider.gender?.contains("Male") == true) "👨‍🔧" else "👩‍🔧",
                    fontSize = 32.sp
                )
                
                // Middle column - Name and Jobs
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = provider.fullName ?: "Unknown",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Jobs: ${provider.totalJobsCompleted ?: 0}",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                
                // Right column - Rating
                Text(
                    text = "${provider.rating ?: 0} Stars",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Edit and Delete buttons taking full width
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Black
                    ),
                    border = BorderStroke(1.dp, Black)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }
                
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Black
                    ),
                    border = BorderStroke(1.dp, Black)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

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
import com.example.assigment.ui.theme.Entity.ServiceProvider
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
    data class Edit(val provider: ServiceProvider) : ProviderPageState()
}

@Composable
fun ProviderScreen(viewModel: ServiceProviderViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var providerPageState by remember { mutableStateOf<ProviderPageState>(ProviderPageState.List) }
    val providers by viewModel.allProviders.collectAsState(initial = emptyList())

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
                        items(providers.filter { it.name?.contains(searchQuery, ignoreCase = true) == true }) { provider ->
                            ProviderCard(
                                provider = provider,
                                onReportToggle = {}
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { providerPageState = ProviderPageState.Edit(provider) },
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
                                    onClick = { viewModel.deleteProvider(provider) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, LightGray),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = White,
                                        contentColor = Black
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Black
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Delete", color = Black)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        is ProviderPageState.Add -> {
            AddProviderScreen(
                onSave = { newProvider ->
                    viewModel.addProvider(newProvider)
                    providerPageState = ProviderPageState.List
                },
                onCancel = { providerPageState = ProviderPageState.List }
            )
        }

        is ProviderPageState.Edit -> {
            val providerToEdit = (providerPageState as ProviderPageState.Edit).provider
            EditProviderScreen(
                provider = providerToEdit,
                onSave = { updatedProvider ->
                    viewModel.updateProvider(updatedProvider)
                    providerPageState = ProviderPageState.List
                },
                onCancel = { providerPageState = ProviderPageState.List }
            )
        }
    }
}

@Composable
fun ProviderCard(provider: ServiceProvider, onReportToggle: () -> Unit) {
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
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LightGray.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (provider.gender == "Female") "👩‍🔧" else "👨‍🔧",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = provider.name ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = provider.serviceType.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightGray
                )
            }

            Text(
                text = "${provider.rating} Star",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            )
        }
    }
}

package com.example.myappuser.ui.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ServiceType {
    PLUMBING,
    CLEANING,
    ELECTRICAL,
    PAINTING,
    CARPENTRY,
    GARDENING
}

enum class UrgencyLevel {
    HIGH,
    MEDIUM,
    LOW
}

@Composable
fun RequestServiceScreen() {
    var selectedServiceType by remember { mutableStateOf<ServiceType?>(ServiceType.PLUMBING) }
    var description by remember { mutableStateOf("") }
    var selectedUrgency by remember { mutableStateOf<UrgencyLevel?>(UrgencyLevel.HIGH) }
    var expandedServiceType by remember { mutableStateOf(false) }
    var expandedUrgency by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Request Service",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Service Type Dropdown
        Text(
            "Service Type",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box {
            OutlinedTextField(
                value = selectedServiceType?.name ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedServiceType = !expandedServiceType }) {
                        Icon(
                            imageVector = if (expandedServiceType) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expandedServiceType,
                onDismissRequest = { expandedServiceType = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                ServiceType.values().forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            selectedServiceType = type
                            expandedServiceType = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
            "Description",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Describe your service request...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Urgency Level Dropdown
        Text(
            "Urgency Level",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box {
            OutlinedTextField(
                value = selectedUrgency?.name ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedUrgency = !expandedUrgency }) {
                        Icon(
                            imageVector = if (expandedUrgency) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expandedUrgency,
                onDismissRequest = { expandedUrgency = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                UrgencyLevel.values().forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level.name) },
                        onClick = {
                            selectedUrgency = level
                            expandedUrgency = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Image Uploader
        Text(
            "Upload Images",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Button(
            onClick = { /* Handle image upload */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0E0E0)
            )
        ) {
            Text(
                "Select Images",
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle payment */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03A9F4) // Light green
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    "Pay",
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { /* Handle submit without payment */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0) // Light gray
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    "Submit Request",
                    color = Color.Black
                )
            }
        }
    }
}
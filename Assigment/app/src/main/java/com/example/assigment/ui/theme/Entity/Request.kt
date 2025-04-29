package com.example.assigment.ui.theme.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class Request(
    @PrimaryKey
    val requestId: String,
    val typeEmoji: String,
    val type: String,
    val providerName: String,
    val time: String,
    val date: String,
    val completed: Boolean,
    val reported: Boolean,
    val rejected: Boolean = false
) 
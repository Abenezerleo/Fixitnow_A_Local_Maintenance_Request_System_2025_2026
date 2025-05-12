package com.example.allapp.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RequesterRequestViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RequesterRequestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RequesterRequestViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 
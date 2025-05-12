package com.example.allapp.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JobRequestViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobRequestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobRequestViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
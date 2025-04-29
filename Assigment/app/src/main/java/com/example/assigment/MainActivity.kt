package com.example.assigment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.assigment.ui.theme.AssigmentTheme
import com.example.assigment.ui.theme.Database.AppDatabase
import com.example.assigment.ui.theme.ViewModel.ServiceProviderViewModel
import com.example.assigment.ui.theme.pages.ProviderScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdminNavGraph()
        }
    }
}

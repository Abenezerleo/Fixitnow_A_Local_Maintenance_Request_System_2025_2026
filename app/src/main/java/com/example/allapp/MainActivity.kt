package com.example.allapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.allapp.ui.theme.AllAppTheme
import com.example.allapp.ui.theme.navigation.AppNavigation
import com.example.allapp.ui.theme.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AllAppTheme {
                // Create NavController
                val navController = rememberNavController()
                // Set up navigation
                AppNavigation(navController = navController)
            }
        }
    }
}
package com.example.allapp.ui.theme.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.allapp.ui.theme.admin.AdminNavGraph
import com.example.allapp.ui.theme.provider.ProviderNavGraph
import com.example.allapp.ui.theme.requester.UserNavGraph
import com.example.allapp.ui.theme.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    val lifecycleOwner = LocalLifecycleOwner.current // Get LifecycleOwner from parent context
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("login") {
            // Provide LifecycleOwner explicitly
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                LoginScreen(navController)
            }
        }
        composable("signup") { SignupScreen(navController) }
        composable("admin") { AdminNavGraph(navController) }
        composable("requester") { UserNavGraph(navController) }
        composable("provider") { ProviderNavGraph(navController) }
    }
}
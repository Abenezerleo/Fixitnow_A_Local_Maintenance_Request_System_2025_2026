package com.example.allapp.ui.theme.provider


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.allapp.ViewModel.NavItem
import kotlin.collections.forEachIndexed
import com.example.allapp.R
import com.example.allapp.ViewModel.AccountViewModel
import com.example.allapp.ViewModel.AccountViewModelFactory

@Composable
fun ProviderNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(context)
    )
    val navItems = listOf(
        NavItem(label = "Application", icon = R.drawable.application, route = "application"),
        NavItem(label = "Suitcase", icon = R.drawable.suitcase, route = "suitcase"),
        NavItem(label = "Management", icon = R.drawable.management, route = "management"),
        NavItem(label = "User", icon = R.drawable.user, route = "user")
    )
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFFC4D8DA))
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "FixItNow",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        viewModel.logout()
                        navController.navigate("welcome") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.Black
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(80.dp),
                containerColor = Color(0xFFC4D8DA),
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, _ ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = navItems[index].icon),
                                contentDescription = navItems[index].label,
                                modifier = Modifier.size(40.dp).padding(bottom = 4.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            indicatorColor = Color(0xFF8EA1A3),
                            unselectedIconColor = Color.Black
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex
        )
    }
}
@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    Box(modifier = modifier.fillMaxSize()) {
        when (selectedIndex) {
            0 -> RequesterScreen()
            1 -> MyJobsScreen()
            2 -> ProfileScreen()
            3 -> ProfileAndSettingScreen()
        }
    }
}



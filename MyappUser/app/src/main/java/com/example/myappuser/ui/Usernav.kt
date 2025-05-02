package com.example.myappuser.ui
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myappuser.R
import com.example.myappuser.RecruitmentScreen
import com.example.myappuser.ui.ui.pages.MyRequestScreen
import com.example.myappuser.ui.ui.pages.RequestServiceScreen
import com.example.myappuser.ui.ui.pages.ProfileScreen

data class NavItem(
    val label: String,
    val icon: Int,
    val route: String
)

@Composable
fun UserNavGraph() {
    val navItems = listOf(
        NavItem(
            label = "Dashboard",
            icon = R.drawable.recruitment,
            route = "dashboard"
        ),
        NavItem(
            label = "Users",
            icon = R.drawable.communication,
            route = "users"
        ),
        NavItem(
            label = "Requests",
            icon = R.drawable.request,
            route = "requests"
        ),
        NavItem(
            label = "Providers",
            icon = R.drawable.user,
            route = "providers"
        )
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
                            indicatorColor = Color(color = 0xFF8EA1A3),
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
            0 -> RecruitmentScreen()
            1 -> MyRequestScreen()
            2 -> RequestServiceScreen()
            3 -> ProfileScreen()
        }
    }
}


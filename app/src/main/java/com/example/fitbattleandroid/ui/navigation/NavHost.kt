package com.example.fitbattleandroid.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitbattleandroid.ui.screen.LoginScreen
import com.websarva.wings.android.myapplication.TopScreen
import com.example.fitbattleandroid.ui.screen.MapScreen

sealed class Screen(val route: String, val title: String) {
    object Map : Screen("map","Map")
    object Top : Screen("top", "Top")
    object Login : Screen("login", "Login")
}

val items =
    listOf(
        Screen.Map,
        Screen.Top,
        Screen.Login,
    )

@Composable
fun Greeting() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.Gray,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            when (screen.route) {
                                "map" -> Icon(Icons.Outlined.Home, contentDescription = "Home")
                                "top" ->
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = "settings",
                                    )
                                "login" ->
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = "Settings",
                                    )
                            }
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Map.route,
            Modifier.padding(innerPadding),
        ) {
            composable(Screen.Map.route) { MapScreen() }
            composable(Screen.Top.route) { TopScreen(navController) }
            composable(Screen.Login.route) { LoginScreen(navController) }
        }
    }
}
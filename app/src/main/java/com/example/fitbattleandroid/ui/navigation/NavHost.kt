package com.example.fitbattleandroid.ui.navigation

import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitbattleandroid.ui.screen.LoginScreen
import com.example.fitbattleandroid.ui.screen.MapScreen
import com.example.fitbattleandroid.ui.screen.RegistrationScreen
import com.example.fitbattleandroid.ui.screen.SampleScreen
import com.example.fitbattleandroid.viewmodel.GeofencingClientViewModel
import com.example.fitbattleandroid.viewmodel.LocationViewModel
import com.websarva.wings.android.myapplication.TopScreen

sealed class Screen(
    val route: String,
    val title: String,
) {
    object Map : Screen("map", "Map")

    object Sample : Screen("sample", "Sample")

    object Top : Screen("top", "Top")

    object Login : Screen("login", "Login")

    object Regi : Screen("regi", "Regi")
}

val items =
    listOf(
        Screen.Map,
        Screen.Sample,
    )

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun App(
    modifier: Modifier,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    locationViewModel: LocationViewModel,
    geofenceViewModel: GeofencingClientViewModel = viewModel(),
    backgroundPermissionGranted: MutableState<Boolean>,
) {
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = Screen.Top.route,
    ) {
        composable(Screen.Top.route) { TopScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Regi.route) { RegistrationScreen(navController) }
        composable("main") {
            MainNavigation(
                requestPermissionLauncher,
                locationViewModel,
                geofenceViewModel,
                backgroundPermissionGranted,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainNavigation(
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    locationViewModel: LocationViewModel,
    geofenceViewModel: GeofencingClientViewModel,
    backgroundPermissionGranted: MutableState<Boolean>,
) {
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
                                "map" -> {
                                    Icon(
                                        Icons.Outlined.LocationOn,
                                        contentDescription = "Map",
                                    )
                                }
                                "sample" -> {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        contentDescription = "settings",
                                    )
                                }
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
            composable(Screen.Map.route) {
                MapScreen(
                    Modifier.padding(innerPadding),
                    requestPermissionLauncher,
                    locationViewModel,
                    geofenceViewModel,
                    backgroundPermissionGranted,
                )
            }
            composable(Screen.Sample.route) { SampleScreen() }
        }
    }
}

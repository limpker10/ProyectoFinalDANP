package com.unsa.edu.proyectofinaldanp.ui.screens.main.bottonBar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.DataItemScreen
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.DataItemViewModel
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.HomeScreen
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.HomeViewModel
import com.unsa.edu.proyectofinaldanp.ui.screens.main.settings.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(HomeViewModel(),navController = navController)
        }
//        composable(route = BottomBarScreen.Profile.route) {
//            ProfileScreen()
//        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
        }
        composable(route = BottomBarScreen.MyOrganization.route) {
            DataItemScreen(DataItemViewModel())
        }
    }
}
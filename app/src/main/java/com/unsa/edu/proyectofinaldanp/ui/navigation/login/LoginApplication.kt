package com.unsa.edu.proyectofinaldanp.ui.navigation.login

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.DataItemScreen
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.DataItemViewModel
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.HomeScreen
import com.unsa.edu.proyectofinaldanp.ui.pagination.views.HomeViewModel
import com.unsa.edu.proyectofinaldanp.ui.screens.login.LoginScreen
import com.unsa.edu.proyectofinaldanp.ui.screens.login.LoginViewModel
import com.unsa.edu.proyectofinaldanp.ui.screens.register.RegisterScreen
import com.unsa.edu.proyectofinaldanp.ui.screens.ResetPassword.ResetScreen
import com.unsa.edu.proyectofinaldanp.ui.screens.main.MainScreen
import com.unsa.edu.proyectofinaldanp.ui.screens.register.RegisterViewModel

@Composable
fun LoginApplication(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_page", builder = {
        composable("login_page", content = { LoginScreen(LoginViewModel(), navController = navController) })
        composable("register_page", content = { RegisterScreen(RegisterViewModel(), navController = navController) })
        composable("reset_page", content = { ResetScreen(navController = navController) })
        composable("Home_page", content = { MainScreen()})
        composable("TempList", content = { DataItemScreen(DataItemViewModel())})

    })
}
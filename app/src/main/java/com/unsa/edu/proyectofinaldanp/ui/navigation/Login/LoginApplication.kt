package com.unsa.edu.proyectofinaldanp.ui.navigation.Login

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.unsa.edu.proyectofinaldanp.ui.Screens.login.LoginScreen
import com.unsa.edu.proyectofinaldanp.ui.Screens.login.LoginViewModel
import com.unsa.edu.proyectofinaldanp.ui.Screens.login.RegisterScreen
import com.unsa.edu.proyectofinaldanp.ui.Screens.login.ResetScreen

@Composable
fun LoginApplication(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_page", builder = {
        composable("login_page", content = { LoginScreen(LoginViewModel(), navController = navController) })
        composable("register_page", content = { RegisterScreen(navController = navController) })
        composable("reset_page", content = { ResetScreen(navController = navController) })
    })
}
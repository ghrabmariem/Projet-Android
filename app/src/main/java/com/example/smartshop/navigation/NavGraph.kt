package com.example.smartshop.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartshop.auth.LoginViewModel
import com.example.smartshop.ui.theme.LoginScreen
import com.example.smartshop.ui.theme.SignUpScreen

@Composable
fun AppNavGraph(navController: NavHostController, loginViewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                navController = navController, // <-- Pass navController here
                onLoginSuccess = { navController.navigate("home") }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = loginViewModel,
                navController = navController, // <-- Pass navController here
                onSignUpSuccess = { navController.navigate("home") }
            )
        }

        composable("home") {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    Text("Bienvenue dans SmartShop !")
}

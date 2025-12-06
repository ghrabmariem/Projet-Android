package com.example.smartshop.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartshop.auth.LoginViewModel
import com.example.smartshop.auth.SignUpViewModel
import com.example.smartshop.ui.theme.LoginScreen
import com.example.smartshop.ui.theme.SignUpScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel
) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { navController.navigate("home") },
                onNavigateSignUp = { navController.navigate("signup") }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = signUpViewModel,
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

package com.example.smartshop.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartshop.auth.LoginViewModel
import com.example.smartshop.auth.SignUpViewModel
import com.example.smartshop.ui.theme.HomeScreen
import com.example.smartshop.ui.LoginScreen
import com.example.smartshop.ui.theme.SignUpScreen
import com.example.smartshop.ui.theme.StatisticsScreen
import com.example.smartshop.viewmodel.ProductViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel,
    productViewModel: ProductViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Écran de connexion
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        // Écran d'inscription
        composable("signup") {
            SignUpScreen(
                viewModel = signUpViewModel,
                onSignUpSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToLogin = {  // <-- use the correct parameter name
                    navController.popBackStack()
                }
            )
        }

        // Écran principal (liste des produits)
        composable("home") {
            HomeScreen(
                viewModel = productViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToStatistics = {
                    navController.navigate("statistics")
                }
            )
        }

        // Écran des statistiques
        composable("statistics") {
            StatisticsScreen(
                viewModel = productViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onBackToLogin = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

    }
}
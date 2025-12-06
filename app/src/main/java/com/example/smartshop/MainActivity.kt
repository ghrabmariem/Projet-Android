package com.example.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.smartshop.auth.LoginViewModel
import com.example.smartshop.auth.SignUpViewModel
import com.example.smartshop.navigation.AppNavGraph
import com.example.smartshop.ui.theme.SmartShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartShopTheme {
                val navController = rememberNavController()
                val loginViewModel: LoginViewModel = viewModel()
                val signUpViewModel: SignUpViewModel = viewModel()

                AppNavGraph(navController, loginViewModel, signUpViewModel)
            }
        }
    }
}

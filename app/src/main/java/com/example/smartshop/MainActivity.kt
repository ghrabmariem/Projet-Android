package com.example.smartshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.smartshop.auth.LoginViewModel
import com.example.smartshop.auth.SignUpViewModel
import com.example.smartshop.data.ProductDatabase
import com.example.smartshop.data.ProductRepository
import com.example.smartshop.navigation.AppNavGraph
import com.example.smartshop.ui.theme.SmartShopTheme
import com.example.smartshop.viewmodel.ProductViewModel
import com.example.smartshop.viewmodel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartShopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // ViewModels d'authentification
                    val loginViewModel = LoginViewModel()
                    val signUpViewModel = SignUpViewModel()

                    // ProductViewModel avec Factory
                    val database = ProductDatabase.getInstance(applicationContext)
                    val repository = ProductRepository(database.productDao())
                    val productViewModel: ProductViewModel = viewModel(
                        factory = ProductViewModelFactory(repository)
                    )

                    // Navigation
                    AppNavGraph(
                        navController = navController,
                        loginViewModel = loginViewModel,
                        signUpViewModel = signUpViewModel,
                        productViewModel = productViewModel
                    )
                }
            }
        }
    }
}
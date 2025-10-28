package com.example.product_app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.product_app.ui.login.LoginScreen
import com.example.product_app.ui.products.ProductDetailScreen
import com.example.product_app.ui.products.ProductListScreen

@Composable
fun NavGraph(startDestination: String = "login") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("productList") {
            ProductListScreen(navController)
        }
        composable(
            "productDetail?productId={productId}",
            arguments = listOf(navArgument("productId") { 
                type = NavType.StringType
                nullable = true 
            })
        ) {
            ProductDetailScreen(navController, it.arguments?.getString("productId"))
        }
    }
}

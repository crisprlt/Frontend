package com.example.product_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.product_app.ui.NavGraph
import com.example.product_app.ui.theme.ProductappTheme
import com.example.product_app.utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(this)
        val startDestination = if (tokenManager.getToken() != null) "productList" else "login"

        setContent {
            ProductappTheme {
                NavGraph(startDestination = startDestination)
            }
        }
    }
}

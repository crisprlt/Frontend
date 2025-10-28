package com.example.product_app.data.models

/**
 * 📝 DATOS PARA LOGIN
 * Lo que enviamos al backend cuando el usuario inicia sesión
 */
data class LoginRequest(
    val email: String,
    val password: String
)
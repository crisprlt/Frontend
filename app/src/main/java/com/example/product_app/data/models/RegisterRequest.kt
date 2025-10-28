package com.example.product_app.data.models

/**
 * 📝 DATOS PARA REGISTRO
 * Lo que enviamos al backend cuando se registra un nuevo usuario
 */
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
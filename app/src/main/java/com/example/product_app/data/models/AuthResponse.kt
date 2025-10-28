package com.example.product_app.data.models

import com.google.gson.annotations.SerializedName

/**
 * 🔑 RESPUESTA DE LOGIN/REGISTER
 * Lo que retorna tu API de Laravel al hacer login
 */
data class AuthResponse(
    val message: String,
    val user: User,
    val token: String,
    @SerializedName("token_type")
    val tokenType: String
)
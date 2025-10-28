package com.example.product_app.data.models

import com.google.gson.annotations.SerializedName

/**
 * 🔄 RESPUESTA PARA REFRESH TOKEN
 * Lo que retorna tu API cuando refrescas el token JWT
 */
data class RefreshTokenResponse(
    val message: String,
    val token: String,
    @SerializedName("token_type")
    val tokenType: String
)
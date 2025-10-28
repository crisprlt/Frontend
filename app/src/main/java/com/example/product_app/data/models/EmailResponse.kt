package com.example.product_app.data.models

import com.google.gson.annotations.SerializedName

data class EmailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)

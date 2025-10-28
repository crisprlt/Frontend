package com.example.product_app.data.models

import com.google.gson.annotations.SerializedName

data class EmailRequest(
    @SerializedName("to") val to: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("body") val body: String
)

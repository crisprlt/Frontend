package com.example.product_app.data.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("price") val price: Double,
    @SerializedName("stock") val stock: Int?,
    @SerializedName("image") val image: String?,
    @SerializedName("created_at") val createdAt: String?,
)

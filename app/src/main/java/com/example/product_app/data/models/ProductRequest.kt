package com.example.product_app.data.models

/**
 * 📝 DATOS PARA CREAR/ACTUALIZAR PRODUCTO
 * Lo que enviamos al backend cuando creamos o editamos un producto
 */
data class ProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val image: String? = null
)
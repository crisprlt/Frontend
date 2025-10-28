package com.example.product_app.data.repositories

import com.example.product_app.data.api.ApiService
import com.example.product_app.data.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val apiService: ApiService) {

    suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getProducts()
            response.body() ?: emptyList()
        }
    }

    suspend fun getProduct(id: Int): Product? {
        return withContext(Dispatchers.IO) {
            val response = apiService.getProduct(id)
            response.body()
        }
    }

    suspend fun createProduct(product: Product): Product? {
        return withContext(Dispatchers.IO) {
            val response = apiService.createProduct(product)
            response.body()
        }
    }

    suspend fun updateProduct(id: Int, product: Product): Product? {
        return withContext(Dispatchers.IO) {
            val response = apiService.updateProduct(id, product)
            response.body()
        }
    }

    suspend fun deleteProduct(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val response = apiService.deleteProduct(id)
            response.isSuccessful
        }
    }
}

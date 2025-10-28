package com.example.product_app.data.api

import com.example.product_app.data.models.EmailRequest
import com.example.product_app.data.models.EmailResponse
import com.example.product_app.data.models.Product
import com.example.product_app.data.models.User
import retrofit2.Response
import retrofit2.http.*

data class LoginResponse(val token: String)
data class RegisterRequest(val name: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @POST("auth/refresh")
    suspend fun refreshToken(): Response<LoginResponse>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<User>

    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<Product>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<Product>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    @POST("notify/email")
    suspend fun sendEmail(@Body request: EmailRequest): Response<EmailResponse>
}

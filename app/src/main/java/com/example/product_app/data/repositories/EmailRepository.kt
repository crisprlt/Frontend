package com.example.product_app.data.repositories

import com.example.product_app.data.api.ApiService
import com.example.product_app.data.models.EmailRequest
import com.example.product_app.data.models.EmailResponse

/**
 * Repository para gestionar el envío de correos a través de la API
 * Responsable de la comunicación con el backend
 */
class EmailRepository(
    private val apiService: ApiService
) {

    /**
     * Envía un correo electrónico a través de la API
     *
     * @param emailRequest Datos del correo (to, subject, body)
     * @return Result con la respuesta de la API o error
     */
    suspend fun sendEmail(emailRequest: EmailRequest): Result<EmailResponse> {
        return try {
            val response = apiService.sendEmail(emailRequest)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                // Manejo de errores HTTP
                val errorMessage = when (response.code()) {
                    400 -> "Datos inválidos en la solicitud"
                    401 -> "No autorizado"
                    403 -> "Acceso prohibido"
                    404 -> "Servicio no encontrado"
                    500 -> "Error interno del servidor"
                    503 -> "Servicio no disponible"
                    else -> "Error al enviar correo (${response.code()})"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: java.net.UnknownHostException) {
            Result.failure(Exception("Sin conexión a internet"))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(Exception("Tiempo de espera agotado"))
        } catch (e: Exception) {
            Result.failure(Exception("Error: ${e.message ?: "Desconocido"}"))
        }
    }
}
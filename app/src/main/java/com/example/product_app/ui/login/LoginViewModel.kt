package com.example.product_app.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.product_app.data.api.ApiService
import com.example.product_app.data.api.LoginRequest
import com.example.product_app.data.api.RetrofitClient
import com.example.product_app.data.models.User
import com.example.product_app.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class LoginViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val apiService: ApiService = RetrofitClient.apiService

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            val totalTime = measureTimeMillis {
                try {
                    Log.d("CONCURRENCY", "Iniciando login concurrente...")


                    val loginTime = measureTimeMillis {
                        val loginDeferred = async(Dispatchers.IO) {
                            apiService.login(LoginRequest(email, password))
                        }
                        val response = loginDeferred.await()

                        if (response.isSuccessful && response.body() != null) {
                            val token = response.body()!!.token

                            val saveTokenTime = measureTimeMillis {
                                withContext(Dispatchers.IO) {
                                    tokenManager.saveToken(token)
                                }
                            }
                            Log.d("CONCURRENCY", "Token guardado en: ${saveTokenTime}ms")

                            loadUserDataConcurrently()

                            _loginState.postValue(LoginState.Success)
                        } else {
                            _loginState.postValue(LoginState.Error("Credenciales inválidas"))
                        }
                    }

                    Log.d("CONCURRENCY", "Login completado en: ${loginTime}ms")

                } catch (e: Exception) {
                    Log.e("CONCURRENCY", "Error en login: ${e.message}")
                    _loginState.postValue(LoginState.Error(e.message ?: "Error desconocido"))
                }
            }

            Log.d("CONCURRENCY", "Tiempo total de login: ${totalTime}ms")
        }
    }
    private fun loadUserDataConcurrently() {
        viewModelScope.launch {
            val userDataTime = measureTimeMillis {
                try {
                    Log.d("CONCURRENCY", "Cargando datos de usuario en paralelo...")

                    val userDeferred = async(Dispatchers.IO) {
                        apiService.getCurrentUser()
                    }

                    val userResponse = userDeferred.await()
                    if (userResponse.isSuccessful) {
                        _currentUser.postValue(userResponse.body())
                        Log.d("CONCURRENCY", "Datos de usuario cargados correctamente")
                    }
                } catch (e: Exception) {
                    Log.w("CONCURRENCY", "Error al cargar datos de usuario: ${e.message}")
                    _currentUser.postValue(null)
                }
            }

            Log.d("CONCURRENCY", "Datos de usuario cargados en: ${userDataTime}ms")
        }
    }
}

sealed class LoginState {
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
    object Loading : LoginState()
}

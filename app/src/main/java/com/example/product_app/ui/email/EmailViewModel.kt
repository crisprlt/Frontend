package com.example.product_app.ui.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.product_app.data.models.EmailRequest
import com.example.product_app.data.repositories.EmailRepository
import kotlinx.coroutines.launch

class EmailViewModel(private val repository: EmailRepository) : ViewModel() {

    private val _emailState = MutableLiveData<EmailState>()
    val emailState: LiveData<EmailState> = _emailState

    fun sendEmail(to: String, subject: String, body: String) {
        viewModelScope.launch {
            _emailState.value = EmailState.Loading
            try {
                val emailRequest = EmailRequest(to, subject, body)
                val result = repository.sendEmail(emailRequest)

                result.onSuccess { response ->
                    _emailState.postValue(EmailState.Success(response.message))
                }.onFailure { error ->
                    _emailState.postValue(EmailState.Error(error.message ?: "Error desconocido"))
                }
            } catch (e: Exception) {
                _emailState.postValue(EmailState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    fun resetState() {
        _emailState.value = EmailState.Idle
    }
}

sealed class EmailState {
    object Idle : EmailState()
    object Loading : EmailState()
    data class Success(val message: String) : EmailState()
    data class Error(val message: String) : EmailState()
}

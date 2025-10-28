package com.example.product_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.product_app.data.api.RetrofitClient
import com.example.product_app.data.repositories.EmailRepository
import com.example.product_app.ui.email.EmailViewModel

class EmailViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailViewModel::class.java)) {
            val repository = EmailRepository(RetrofitClient.apiService)
            @Suppress("UNCHECKED_CAST")
            return EmailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

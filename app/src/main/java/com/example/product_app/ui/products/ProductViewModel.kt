package com.example.product_app.ui.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.product_app.data.models.Product
import com.example.product_app.data.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> = _product

    private val _productState = MutableLiveData<ProductState>()
    val productState: LiveData<ProductState> = _productState

    private val _productDetailState = MutableLiveData<ProductDetailState>()
    val productDetailState: LiveData<ProductDetailState> = _productDetailState

    fun getProducts() {
        viewModelScope.launch {
            _productState.value = ProductState.Loading
            try {

                val productList = repository.getProducts()
                _products.postValue(productList)
                _productState.postValue(ProductState.Success)
            } catch (e: Exception) {
                _productState.postValue(ProductState.Error(e.message ?: "Error desconocido"))
            }
        }
    }


    fun loadMultipleProductsConcurrently(productIds: List<Int>) {
        viewModelScope.launch {
            _productState.value = ProductState.Loading

            val totalTime = measureTimeMillis {
                try {
                    Log.d("CONCURRENCY", "Cargando ${productIds.size} productos EN PARALELO...")


                    val deferredProducts = productIds.map { id ->
                        async(Dispatchers.IO) {
                            repository.getProduct(id)
                        }
                    }

                    val loadedProducts = deferredProducts.awaitAll().filterNotNull()

                    _products.postValue(loadedProducts)
                    _productState.postValue(ProductState.Success)

                    Log.d("CONCURRENCY", "${loadedProducts.size} productos cargados exitosamente")
                } catch (e: Exception) {
                    Log.e("CONCURRENCY", "Error al cargar productos: ${e.message}")
                    _productState.postValue(ProductState.Error(e.message ?: "Error al cargar productos"))
                }
            }

            Log.d("CONCURRENCY", "${productIds.size} productos cargados en PARALELO en: ${totalTime}ms")
            Log.d("CONCURRENCY", "Promedio por producto: ${totalTime / productIds.size}ms")
        }
    }

    fun getProduct(id: Int) {
        viewModelScope.launch {
            _productDetailState.value = ProductDetailState.Loading
            try {
                val product = repository.getProduct(id)
                _product.postValue(product)
                _productDetailState.postValue(ProductDetailState.Success)
            } catch (e: Exception) {
                _productDetailState.postValue(ProductDetailState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun createProduct(product: Product) {
        viewModelScope.launch {
            _productDetailState.value = ProductDetailState.Loading
            try {
                repository.createProduct(product)
                _productDetailState.postValue(ProductDetailState.Success)
            } catch (e: Exception) {
                _productDetailState.postValue(ProductDetailState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun updateProduct(id: Int, product: Product) {
        viewModelScope.launch {
            _productDetailState.value = ProductDetailState.Loading
            try {
                repository.updateProduct(id, product)
                _productDetailState.postValue(ProductDetailState.Success)
            } catch (e: Exception) {
                _productDetailState.postValue(ProductDetailState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            _productDetailState.value = ProductDetailState.Loading
            try {
                repository.deleteProduct(id)
                _productDetailState.postValue(ProductDetailState.Success)
            } catch (e: Exception) {
                _productDetailState.postValue(ProductDetailState.Error(e.message ?: "Unknown error"))
            }
        }
    }
}

sealed class ProductState {
    object Success : ProductState()
    data class Error(val message: String) : ProductState()
    object Loading : ProductState()
}

sealed class ProductDetailState {
    object Success : ProductDetailState()
    data class Error(val message: String) : ProductDetailState()
    object Loading : ProductDetailState()
    object Deleted: ProductDetailState()
}

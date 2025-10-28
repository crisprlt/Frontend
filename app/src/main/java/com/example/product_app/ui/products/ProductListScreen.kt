package com.example.product_app.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.product_app.ProductApplication
import com.example.product_app.data.models.Product
import com.example.product_app.ui.email.SendEmailDialog
import com.example.product_app.ui.viewmodels.ProductViewModelFactory
import com.example.product_app.utils.TokenManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavController) {
    val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory())
    val products by productViewModel.products.observeAsState(emptyList())
    val productState by productViewModel.productState.observeAsState()
    val tokenManager = TokenManager(ProductApplication.instance)

    var showEmailDialog by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(navController.currentBackStackEntry) {
        productViewModel.getProducts()
    }

    // Mostrar Snackbar cuando hay un mensaje
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            snackbarMessage = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                actions = {
                    // Botón de enviar correo
                    IconButton(onClick = {
                        showEmailDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Enviar correo"
                        )
                    }

                    // Botón de cerrar sesión
                    IconButton(onClick = {
                        tokenManager.clearToken()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar Sesión"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("productDetail") }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (productState) {
                is ProductState.Loading -> CircularProgressIndicator()
                is ProductState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(products) {
                            ProductItem(product = it) {
                                navController.navigate("productDetail?productId=${it.id}")
                            }
                        }
                    }
                }
                is ProductState.Error -> {
                    val message = (productState as ProductState.Error).message
                    Text(text = "Error: $message")
                }
                else -> {}
            }
        }
    }

    // Mostrar diálogo de envío de correo
    if (showEmailDialog) {
        SendEmailDialog(
            onDismiss = { showEmailDialog = false },
            onEmailSent = { message ->
                snackbarMessage = message
            }
        )
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.image ?: "https://via.placeholder.com/100x100?text=Sin+Imagen",
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description ?: "Sin descripción",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio: $${product.price}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Stock: ${product.stock ?: 0}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}


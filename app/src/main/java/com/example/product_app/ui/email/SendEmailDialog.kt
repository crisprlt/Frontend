package com.example.product_app.ui.email

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.product_app.ui.viewmodels.EmailViewModelFactory

@Composable
fun SendEmailDialog(
    onDismiss: () -> Unit,
    onEmailSent: (String) -> Unit
) {
    val emailViewModel: EmailViewModel = viewModel(factory = EmailViewModelFactory())
    val emailState by emailViewModel.emailState.observeAsState(EmailState.Idle)

    var toEmail by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var toEmailError by remember { mutableStateOf(false) }
    var subjectError by remember { mutableStateOf(false) }
    var bodyError by remember { mutableStateOf(false) }

    // Observar el estado y manejar respuestas
    LaunchedEffect(emailState) {
        when (emailState) {
            is EmailState.Success -> {
                val message = (emailState as EmailState.Success).message
                onEmailSent(message)
                emailViewModel.resetState()
                onDismiss()
            }
            is EmailState.Error -> {
                // El error se mostrará en el diálogo
            }
            else -> {}
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (emailState !is EmailState.Loading) {
                emailViewModel.resetState()
                onDismiss()
            }
        },
        title = {
            Text("Enviar correo")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Campo "Para"
                OutlinedTextField(
                    value = toEmail,
                    onValueChange = {
                        toEmail = it
                        toEmailError = false
                    },
                    label = { Text("Para") },
                    placeholder = { Text("destinatario@ejemplo.com") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = emailState !is EmailState.Loading,
                    isError = toEmailError,
                    supportingText = {
                        if (toEmailError) {
                            Text("El correo es requerido")
                        }
                    },
                    singleLine = true
                )

                // Campo "Asunto"
                OutlinedTextField(
                    value = subject,
                    onValueChange = {
                        subject = it
                        subjectError = false
                    },
                    label = { Text("Asunto") },
                    placeholder = { Text("Asunto del correo") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = emailState !is EmailState.Loading,
                    isError = subjectError,
                    supportingText = {
                        if (subjectError) {
                            Text("El asunto es requerido")
                        }
                    },
                    singleLine = true
                )

                // Campo "Mensaje"
                OutlinedTextField(
                    value = body,
                    onValueChange = {
                        body = it
                        bodyError = false
                    },
                    label = { Text("Mensaje") },
                    placeholder = { Text("Escribe tu mensaje aquí") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    enabled = emailState !is EmailState.Loading,
                    isError = bodyError,
                    supportingText = {
                        if (bodyError) {
                            Text("El mensaje es requerido")
                        }
                    },
                    maxLines = 5
                )

                // Mostrar error si hay
                if (emailState is EmailState.Error) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = (emailState as EmailState.Error).message,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                // Mostrar spinner durante el envío
                if (emailState is EmailState.Loading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Enviando correo...")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Validar campos
                    toEmailError = toEmail.isBlank()
                    subjectError = subject.isBlank()
                    bodyError = body.isBlank()

                    if (!toEmailError && !subjectError && !bodyError) {
                        emailViewModel.sendEmail(toEmail.trim(), subject.trim(), body.trim())
                    }
                },
                enabled = emailState !is EmailState.Loading
            ) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    emailViewModel.resetState()
                    onDismiss()
                },
                enabled = emailState !is EmailState.Loading
            ) {
                Text("Cancelar")
            }
        }
    )
}

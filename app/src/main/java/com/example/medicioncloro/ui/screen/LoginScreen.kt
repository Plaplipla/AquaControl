package com.example.medicioncloro.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medicioncloro.R
import com.example.medicioncloro.ui.theme.*
import com.example.medicioncloro.ui.viewmodel.SessionViewModel

@Composable
fun LoginScreen(sessionViewModel: SessionViewModel) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val error by sessionViewModel.errorLogin.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. LA IMAGEN DE FONDO
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. EL FORMULARIO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Títulos centrados
            Text(
                text = "AquaControl",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CorporateDarkBlue,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Medición de cloro en línea.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = CorporateTextDark,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // TARJETA BLANCA
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth() // Mantiene la tarjeta ancha
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CorporateDarkBlue,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it; sessionViewModel.limpiarError() },
                        label = { Text("Correo corporativo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CorporateDarkBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryWaterBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = CorporateTextDark,
                            unfocusedTextColor = CorporateTextDark
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; sessionViewModel.limpiarError() },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CorporateDarkBlue) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = CorporateTextGray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryWaterBlue,
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = CorporateTextDark,
                            unfocusedTextColor = CorporateTextDark
                        )
                    )

                    if (error != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(text = error!!, color = CorporateRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { sessionViewModel.login(correo, password) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryWaterBlue)
                    ) {
                        Text("Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Ayuda para el profe
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Credenciales de prueba:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CorporateDarkBlue)
                    Text("operario@empresa.cl / 123456", fontSize = 12.sp, color = CorporateTextGray)
                    Text("supervisor@empresa.cl / 123456", fontSize = 12.sp, color = CorporateTextGray)
                    Text("jefatura@empresa.cl / 123456", fontSize = 12.sp, color = CorporateTextGray)
                }
            }
        }
    }
}
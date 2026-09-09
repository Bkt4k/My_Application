package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════════
//  COLORES
// ═══════════════════════════════════════════════════════════
object AppColors {
    val fondoMorado = Color(0xFF6B2D5B)
    val fondoAzul = Color(0xFF2D3A6B)
    val fondoRosa = Color(0xFF8B3A6B)
    val tarjetaFondo = Color(0x80FFFFFF)
    val textoOscuro = Color(0xFF2D2D4E)
    val textoClaro = Color(0xFF6B6B8E)
    val botonInicio = Color(0xFF4A3B8F)
    val botonFin = Color(0xFF6B5ACD)
    val exito = Color(0xFF4CAF50)
    val error = Color(0xFFF44336)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

// ═══════════════════════════════════════════════════════════
//  NAVEGACIÓN SIMPLE
// ═══════════════════════════════════════════════════════════
@Composable
fun AppNavigation() {
    // Estado que define qué pantalla mostrar
    var pantallaActual by remember { mutableStateOf("login") } // "login" o "dashboard"
    var usuarioLogueado by remember { mutableStateOf<Usuario?>(null) }

    when (pantallaActual) {
        "login" -> LoginScreen(
            onLoginExitoso = { usuario ->
                usuarioLogueado = usuario
                pantallaActual = "dashboard"
            }
        )
        "dashboard" -> DashboardScreen(
            usuario = usuarioLogueado!!,
            onLogout = {
                usuarioLogueado = null
                pantallaActual = "login"
            }
        )
    }
}

// ═══════════════════════════════════════════════════════════
// 📱 PANTALLA DE LOGIN
// ═══════════════════════════════════════════════════════════
@Composable
fun LoginScreen(onLoginExitoso: (Usuario) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.fondoMorado,
                        AppColors.fondoAzul,
                        AppColors.fondoRosa
                    )
                )
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.Center)
                .padding(24.dp),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.tarjetaFondo),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(AppColors.textoClaro.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "", fontSize = 50.sp)
                }

                Text(
                    text = "GAME ZONE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.textoOscuro
                )

                // Campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = AppColors.textoClaro) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.textoOscuro,
                        unfocusedBorderColor = AppColors.textoClaro
                    )
                )

                // Campo Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = AppColors.textoClaro) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AppColors.textoOscuro,
                        unfocusedBorderColor = AppColors.textoClaro
                    )
                )

                // Mensaje de error
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = AppColors.error,
                        fontSize = 14.sp
                    )
                }

                // Botón Login
                Button(
                    onClick = {
                        val usuario = UserRepository.validarLogin(email, password)
                        if (usuario != null) {
                            onLoginExitoso(usuario)
                        } else {
                            errorMessage = "Email o contraseña incorrectos"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.botonInicio
                    )
                ) {
                    Text(
                        text = "LOGIN",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Texto de ayuda
                Text(
                    text = "Usuarios demo:\nadmin@gmail.com / 123456",
                    fontSize = 12.sp,
                    color = AppColors.textoClaro
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// 🎮 PANTALLA DASHBOARD
// ═══════════════════════════════════════════════════════════
@Composable
fun DashboardScreen(usuario: Usuario, onLogout: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.fondoAzul,
                        AppColors.fondoMorado,
                        AppColors.fondoRosa
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎮 Dashboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColors.error
                    )
                ) {
                    Text(text = "Salir", color = Color.White)
                }
            }

            // Tarjeta de bienvenida
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.tarjetaFondo
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¡Bienvenido!",
                        fontSize = 20.sp,
                        color = AppColors.textoClaro
                    )
                    Text(
                        text = usuario.nombre,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.textoOscuro
                    )
                    Text(
                        text = usuario.email,
                        fontSize = 14.sp,
                        color = AppColors.textoClaro
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats del juego
            Text(
                text = "📊 Tus Estadísticas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grid de estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),  // ← Agrega esto
                    titulo = "Partidas",
                    valor = "42",
                    icono = "🎮"
                )
                StatCard(
                    modifier = Modifier.weight(1f),  // ← Agrega esto
                    titulo = "Victorias",
                    valor = "38",
                    icono = "🏆"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(titulo = "Nivel", valor = "15", icono = "⭐")
                StatCard(titulo = "Puntos", valor = "2,450", icono = "💎")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Text(
                text = "© 2024 Game Zone",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════
//  TARJETA DE ESTADÍSTICA
// ═══════════════════════════════════════════════════════════
@Composable
fun StatCard(modifier: Modifier = Modifier,titulo: String, valor: String, icono: String) {
    Card(
        modifier = Modifier
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.tarjetaFondo
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icono, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = valor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.textoOscuro
            )
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = AppColors.textoClaro
            )
        }
    }
}
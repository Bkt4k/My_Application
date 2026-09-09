package com.example.myapplication

// Modelo de Usuario
data class Usuario(
    val email: String,
    val password: String,
    val nombre: String
)

// "Base de datos" simple - Usuarios registrados
object UserRepository {
    // Lista de usuarios (aquí están los "registrados")
    private val usuariosRegistrados = listOf(
        Usuario("admin@gmail.com", "123456", "Administrador"),
        Usuario("usuario@gmail.com", "123456", "Usuario Demo"),
        Usuario("gamer@gmail.com", "gamer123", "Gamer Pro")
    )

    // Función para validar login
    fun validarLogin(email: String, password: String): Usuario? {
        return usuariosRegistrados.find {
            it.email == email && it.password == password
        }
    }

    // Función para registrar nuevo usuario (simple)
    fun registrarUsuario(email: String, password: String, nombre: String): Boolean {
        // Verificar si el email ya existe
        if (usuariosRegistrados.any { it.email == email }) {
            return false // Email ya registrado
        }

        // Agregar nuevo usuario
        usuariosRegistrados.plus(Usuario(email, password, nombre))
        return true
    }
}
package com.devYoussef.nb3elkhieradmin.model

data class AuthState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
    val status :String? = null,
    val login :AuthResponse? = null,
)

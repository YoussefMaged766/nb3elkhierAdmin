package com.devYoussef.nb3elkhieradmin.model


data class LoginModel(
    val userName: String? = null,
    val password: String? = null,
    val fcmToken :String? = null,
    val region: String? = null,
    val limit:Double? = null,
    val country: String? = null,
    val orderId: String? = null,
    val productId: String? = null
)

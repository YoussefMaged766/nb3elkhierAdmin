package com.devYoussef.nb3elkhieradmin.model

data class AuthState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
    val status: String? = null,
    val login: AuthResponse? = null,
    val statistics: StatisticsResponse? = null,
    val category: CategoryResponse? = null,
    val product: ProductResponse? = null,
    val promo : PromoCodeResponse? = null,
    val order : OrderResponse? = null,
    val orderDetails: OrderDetailsResponse? = null,
    val block: BlockUsersResponse? = null,
)

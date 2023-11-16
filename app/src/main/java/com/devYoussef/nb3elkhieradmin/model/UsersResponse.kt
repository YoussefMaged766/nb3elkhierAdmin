package com.devYoussef.nb3elkhieradmin.model

data class UsersResponse(
    val status: String?,
    val user: List<User?>?
) {
    data class User(
        val __v: Int?,
        val _id: String?,
        val country: String?,
        val createdAt: String?,
        val email: String?,
        val fcmToken: List<String?>?,
        val governorate: String?,
        val isBlocked: Boolean?,
        val orderId: List<Any?>?,
        val ordersId: List<OrdersId?>?,
        val password: String?,
        val phone: String?,
        val region: String?,
        val role: String?,
        val image: Image?,
        val shopAddress: String?,
        val shopName: String?,
        val updatedAt: String?,
        val userName: String?,
        val wishlist: List<Any?>?
    ) {
        data class OrdersId(
            val __v: Int?,
            val _id: String?,
            val address: String?,
            val createdAt: String?,
            val isCanceled: Boolean?,
            val isDelivered: Boolean?,
            val note: String?,
            val orderNum: String?,
            val phone: String?,
            val products: List<Product?>?,
            val promoCode: String?,
            val quantity: Int?,
            val totalPrice: Int?,
            val updatedAt: String?,
            val userId: String?
        ) {
            data class Product(
                val _id: String?,
                val productId: String?,
                val quantity: Int?,
                val totalProductPrice: Int?
            )
        }

        data class Image(
            val public_id: String?,
            val url: String?
        )
    }
}
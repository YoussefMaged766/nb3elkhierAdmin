package com.devYoussef.nb3elkhieradmin.model

data class OrderDetailsResponse(
    val status: String?,
    val userOrder: List<UserOrder>?
) {
    data class UserOrder(
        val __v: Int?,
        val _id: String?,
        val address: String?,
        val createdAt: String?,
        val isCanceled: Boolean?,
        val isDelivered: Boolean?,
        val note: String?,
        val orderNum: String?,
        val phone: String?,
        val products: List<Product>?,
        val promoCode: PromoCode?,
        val quantity: Int?,
        val totalPrice: Double?,
        val updatedAt: String?,
        val userId: UserId?
    ) {
        data class Product(
            val _id: String?,
            val productId: ProductId?,
            val quantity: Int?,
            val totalProductPrice: Double?
        ) {
            data class ProductId(
                val __v: Int?,
                val _id: String?,
                val category: String?,
                val country: String?,
                val createdAt: String?,
                val image: Image?,
                val isAvailable: Boolean?,
                val isOffered: Boolean?,
                val name: String?,
                val offer: List<Offer?>?,
                val originalPrice: Double?,
                val price: Double?,
                val priceCurrency: String?,
                val quantity: Int?,
                val shortDescription: String?,
                val updatedAt: String?
            ) {
                data class Image(
                    val public_id: String?,
                    val url: String?
                )

                data class Offer(
                    val _id: String?,
                    val itemNum: Int?,
                    val priceOffered: Double?
                )
            }
        }

        data class PromoCode(
            val __v: Int?,
            val _id: String?,
            val code: String?,
            val createdAt: String?,
            val isActive: Boolean?,
            val price: Double?,
            val timesNum: Int?,
            val updatedAt: String?,
            val usedNum: Int?,
            val userId: List<String?>?
        )
        data class UserId(
            val __v: Int?,
            val _id: String?,
            val country: String?,
            val createdAt: String?,
            val email: String?,
            val fcmToken: List<String?>?,
            val governorate: String?,
            val image: Image?,
            val isBlocked: Boolean?,
            val ordersId: List<String?>?,
            val password: String?,
            val phone: String?,
            val region: String?,
            val role: String?,
            val shopAddress: String?,
            val shopName: String?,
            val updatedAt: String?,
            val userName: String?,
            val wishlist: List<Any?>?
        ) {
            data class Image(
                val public_id: String?,
                val url: String?
            )
        }
    }
}
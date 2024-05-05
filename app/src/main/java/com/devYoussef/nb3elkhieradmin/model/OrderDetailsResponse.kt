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
        val products: List<Product?>?,
        val promoCode: PromoCode?,
        val quantity: Int?,
        val revenue: Int?,
        val totalPrice: Int?,
        val updatedAt: String?,
        val userId: UserId?,
        val location: Location?
    ) {
        data class Location(
            val latitude: Double?,
            val longitude: Double?
        )


        data class Product(
            val _id: String?,
            val productId: ProductId?,
            val productPrice: Int?,
            val quantity: Int?,
            val totalProductPrice: Int?,
            val offer: Offer?
        ) {
            data class Offer(
                val offerItems: Int?,
                val offerPrice: Int?
            )
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
                val originalPrice: Int?,
                val price: Int?,
                val priceCurrency: String?,
                val quantity: Int?,
                val quantityLimit: Int?,
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
                    val priceOffered: Int?
                )
            }
            fun isHasOffer(): Boolean {
                return offer != null
            }
            fun getQuantityAfterOffer(): Int {
                return quantity!!.minus(offer?.offerItems ?: 0)
            }
            fun isQuantityLowerThanOffer(): Boolean {
                return quantity!! < offer?.offerItems!!
            }
            fun calculateOfferPrice(): Int {
                return getQuantityAfterOffer() * offer?.offerPrice!!
            }

            fun calculatePrice(): Int {
                return getQuantityAfterOffer() * productPrice!!
            }
        }

        data class PromoCode(
            val __v: Int?,
            val _id: String?,
            val code: String?,
            val createdAt: String?,
            val isActive: Boolean?,
            val price: Int?,
            val timesNum: Int?,
            val unitValue: String?,
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
            val isBlocked: Boolean?,
            val isCompleted: Boolean?,
            val location: Location?,
            val ordersId: List<String?>?,
            val password: String?,
            val phone: String?,
            val region: String?,
            val resendCodeCount: Int?,
            val role: String?,
            val shopAddress: String?,
            val shopName: String?,
            val updatedAt: String?,
            val userName: String?
        ) {
            data class Location(
                val latitude: Double?,
                val longitude: Double?
            )
        }
    }
}


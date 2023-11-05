package com.devYoussef.nb3elkhieradmin.model

data class ProductResponse(
    val `data`: List<Data>?,
    val status: String?
) {
    data class Data(
        val _id: String?,
        val category: String?,
        val country: String?,
        val createdAt: String?,
        val image: String?,
        val isAvailable: Boolean?,
        val isOffered: Boolean?,
        val name: String?,
        val offer: List<Offer>?,
        val originalPrice: Int?,
        val price: Int?,
        val priceCurrency: String?,
        val quantity: Int?,
        val shortDescription: String?,
        val updatedAt: String?
    ) {
        data class Offer(
            val _id: String?,
            val itemNum: Int?,
            val priceOffered: Int?
        )
    }
}
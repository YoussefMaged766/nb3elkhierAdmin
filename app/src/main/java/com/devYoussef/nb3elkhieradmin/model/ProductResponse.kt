package com.devYoussef.nb3elkhieradmin.model

data class ProductResponse(
    val `data`: List<Data>?,
    val status: String?,
    val page:Int?
) {
    data class Data(
        val __v: Int?,
        val _id: String?,
        val category: String?,
        val country: String?,
        val createdAt: String?,
        val image: Image?,
        val isAvailable: Boolean?,
        val isOffered: Boolean?,
        val name: String?,
        val offer: List<Offer>?,
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
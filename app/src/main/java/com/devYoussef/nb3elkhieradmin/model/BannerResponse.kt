package com.devYoussef.nb3elkhieradmin.model

data class BannerResponse(
    val `data`: List<Data>?,
    val status: String?
) {
    data class Data(
        val __v: Int?,
        val _id: String?,
        val createdAt: String?,
        val image: Image?,
        val updatedAt: String?
    ) {
        data class Image(
            val public_id: String?,
            val url: String?
        )
    }
}
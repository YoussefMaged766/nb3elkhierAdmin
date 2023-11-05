package com.devYoussef.nb3elkhieradmin.model

data class CategoryResponse(
    val `data`: List<Data>?,
    val status: String?
) {
    data class Data(
        val __v: Int?,
        val _id: String?,
        val createdAt: String?,
        val image: String?,
        val name: String? = null,
        val updatedAt: String?
    )
}
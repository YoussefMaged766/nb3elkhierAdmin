package com.devYoussef.nb3elkhieradmin.model

data class PromoCodeResponse(
    val counter: Int?,
    val `data`: List<Data>?,
    val status: String?
) {
    data class Data(
        val __v: Int?,
        val _id: String?,
        val code: String?,
        val createdAt: String?,
        val isActive: Boolean?,
        val price: Int?,
        val timesNum: Int?,
        val updatedAt: String?,
        val usedNum: Int?,
        val userId: List<String>?
    )
}
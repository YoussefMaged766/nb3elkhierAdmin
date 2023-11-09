package com.devYoussef.nb3elkhieradmin.model

data class RegionsResponse(
    val `data`: List<Data>?,
    val status: String?
) {
    data class Data(
        val __v: Int?,
        val _id: String?,
        val country: String?,
        val limit: String?,
        val region: String?
    )
}
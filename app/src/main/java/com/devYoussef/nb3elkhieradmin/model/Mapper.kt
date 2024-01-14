package com.devYoussef.nb3elkhieradmin.model



data class dummyProduct(
    val page:Int,
    val data:ProductResponse.Data
)
fun ProductResponse.Data.toDomain(page: Int):dummyProduct{
    return dummyProduct(
        page = page,
        data = this
    )
}
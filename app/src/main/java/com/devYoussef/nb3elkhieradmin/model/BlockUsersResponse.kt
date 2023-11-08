package com.devYoussef.nb3elkhieradmin.model

data class BlockUsersResponse(
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
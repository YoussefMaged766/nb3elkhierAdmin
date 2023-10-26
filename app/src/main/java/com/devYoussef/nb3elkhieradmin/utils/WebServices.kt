package com.devYoussef.nb3elkhieradmin.utils

import com.devYoussef.nb3elkhieradmin.model.AuthResponse
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import retrofit2.http.Body
import retrofit2.http.POST

interface WebServices {

    @POST("api/manage/login")
    suspend fun login(
        @Body user: LoginModel
    ): AuthResponse
}
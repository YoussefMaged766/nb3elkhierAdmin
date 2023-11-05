package com.devYoussef.nb3elkhieradmin.utils

import com.devYoussef.nb3elkhieradmin.model.AuthResponse
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.model.StatisticsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WebServices {

    @POST("api/manage/login")
    suspend fun login(
        @Body user: LoginModel
    ): AuthResponse

    @GET("api/product/get")
    suspend fun getProducts(@Query("page") page: Int): ProductResponse

    @GET("api/product/search")
    suspend fun getSearch(@Query("key") query: String, @Query("page") page: Int): ProductResponse

    @DELETE("api/product/delete/{id}")
    suspend fun deleteProduct(@Path("id") id: String): AuthResponse

    @GET("api/statistics")
    suspend fun getStatistics():StatisticsResponse
}
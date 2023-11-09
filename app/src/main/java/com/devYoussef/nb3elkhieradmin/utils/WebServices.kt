package com.devYoussef.nb3elkhieradmin.utils

import com.devYoussef.nb3elkhieradmin.model.AuthResponse
import com.devYoussef.nb3elkhieradmin.model.BannerResponse
import com.devYoussef.nb3elkhieradmin.model.BlockUsersResponse
import com.devYoussef.nb3elkhieradmin.model.CategoryResponse
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import com.devYoussef.nb3elkhieradmin.model.OrderDetailsResponse
import com.devYoussef.nb3elkhieradmin.model.OrderResponse
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.model.PromoCodeModel
import com.devYoussef.nb3elkhieradmin.model.PromoCodeResponse
import com.devYoussef.nb3elkhieradmin.model.RegionsResponse
import com.devYoussef.nb3elkhieradmin.model.StatisticsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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
    suspend fun getStatistics(): StatisticsResponse

    @GET("api/manage/category/all")
    suspend fun getCategory(): CategoryResponse

    @GET("api/product/get/{id}")
    suspend fun getOneProduct(@Path("id") id: String): ProductResponse

    @Multipart
    @POST("api/product/add")
    suspend fun addProduct(
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("shortDescription") shortDescription: RequestBody,
        @Part("price") price: RequestBody,
        @Part("originalPrice") originalPrice: RequestBody,
        @Part("priceCurrency") priceCurrency: RequestBody,
        @Part("country") country: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part("quantity") quantity: RequestBody,
        @Part("isAvailable") isAvailable: RequestBody,
        @Part("isOffered") isOffered: RequestBody,
        @Part("offer[0][priceOffered]") priceOffered: RequestBody,
        @Part("offer[0][itemNum]") itemNum: RequestBody,
    ): AuthResponse

    @Multipart
    @PATCH("api/product/update/{id}")
    suspend fun updateProduct(
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("shortDescription") shortDescription: RequestBody,
        @Part("price") price: RequestBody,
        @Part("originalPrice") originalPrice: RequestBody,
        @Part("priceCurrency") priceCurrency: RequestBody,
        @Part("country") country: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part("quantity") quantity: RequestBody,
        @Part("isAvailable") isAvailable: RequestBody,
        @Part("isOffered") isOffered: RequestBody,
        @Part("offer[0][priceOffered]") priceOffered: RequestBody,
        @Part("offer[0][itemNum]") itemNum: RequestBody,
        @Path("id") id: String
    ): AuthResponse

    @POST("api/manage/promo/add")
    suspend fun addPromoCode(@Body model: PromoCodeModel): AuthResponse

    @DELETE("api/manage/promo/delete/{key}")
    suspend fun deletePromoCode(@Path("key") id: String): AuthResponse

    @PATCH("api/manage/promo/update/{id}")
    suspend fun updatePromoCode(@Body model: PromoCodeModel, @Path("id") id: String): AuthResponse

    @GET("api/manage/promo/all?")
    suspend fun getAllPromoCode(): PromoCodeResponse

    @GET("api/manage/promo/one/{id}")
    suspend fun getOnePromoCode(@Path("id") id: String): PromoCodeResponse

    @GET("api/order/all-orders")
    suspend fun getAllOrders(): OrderResponse

    @GET("api/order/one-order/{id}")
    suspend fun getOrderDetails(@Path("id") id: String): OrderDetailsResponse

    @GET("api/order/accept/{id}")
    suspend fun acceptOrder(@Path("id") id: String): AuthResponse

    @GET("api/order/cancel/{id}")
    suspend fun cancelOrder(@Path("id") id: String): AuthResponse

    @GET("api/user/all")
    suspend fun getAllBlockedUsers(): BlockUsersResponse

    @GET("api/manage/block/{id}")
    suspend fun blockUser(@Path("id") id: String): AuthResponse

    @GET("api/manage/banner/all?")
    suspend fun getAllBanner(): BannerResponse

    @Multipart
    @POST("api/manage/banner/add")
    suspend fun addBanner(
        @Part image: MultipartBody.Part? = null,
    ): AuthResponse

    @DELETE("api/manage/banner/delete/{id}")
    suspend fun deleteBanner(@Path("id") id: String): AuthResponse

    @POST("api/manage/region/add")
    suspend fun addRegion(@Body model: LoginModel): AuthResponse

    @PATCH("api/manage/region/update/{id}")
    suspend fun updateRegion(@Body model: LoginModel , @Path("id") id: String): AuthResponse

    @GET("api/manage/region/one/{id}")
    suspend fun getOneRegion(@Path("id") id: String): RegionsResponse

    @GET("api/manage/region/all")
    suspend fun getAllRegions(): RegionsResponse

    @DELETE("api/manage/region/delete/{id}")
    suspend fun deleteRegion(@Path("id") id: String): AuthResponse


}
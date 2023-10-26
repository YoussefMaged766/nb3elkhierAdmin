package com.devYoussef.nb3elkhieradmin.data.local


import com.devYoussef.nb3elkhieradmin.constant.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val dataStoreRepository: DataStoreRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = runBlocking {
            val token = dataStoreRepository.getToken(Constants.TOKEN)
            if (token != null) {
                originalRequest.newBuilder()
                    .header("cookie", "jwt=$token")
                    .build()
            } else {
                originalRequest
            }
        }

        return chain.proceed(newRequest)
    }
}


package com.devYoussef.nb3elkhieradmin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.devYoussef.nb3elkhieradmin.constant.Constants
import com.devYoussef.nb3elkhieradmin.constant.Constants.dataStore
import com.devYoussef.nb3elkhieradmin.data.local.DataStoreRepository
import com.devYoussef.nb3elkhieradmin.data.local.TokenInterceptor
import com.devYoussef.nb3elkhieradmin.data.remote.Repo
import com.devYoussef.nb3elkhieradmin.utils.WebServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepository(dataStore)
    }


    @Provides
    @Singleton
    fun provideTokenInterceptor(dataStoreRepository: DataStoreRepository): TokenInterceptor {
        return TokenInterceptor(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun provideHttp(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): WebServices {
        return retrofit.create(WebServices::class.java)
    }

    @Provides
    @Singleton
    fun getRepo(webServices: WebServices, @ApplicationContext context: Context): Repo {
        return Repo(webServices, context)
    }

    @Provides
    @Singleton
    fun getJson(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

}
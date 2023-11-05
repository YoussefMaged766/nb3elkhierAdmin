package com.devYoussef.nb3elkhieradmin.data.remote

import android.content.Context
import android.util.Log
import com.devYoussef.nb3elkhieradmin.model.AuthResponse
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import com.devYoussef.nb3elkhieradmin.utils.NetworkUtils
import com.devYoussef.nb3elkhieradmin.utils.Status
import com.devYoussef.nb3elkhieradmin.utils.WebServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class Repo @Inject constructor(
    private val webServices: WebServices,
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()
    fun loginIn(loginModel: LoginModel) = flow {
        if (NetworkUtils(context).isNetworkConnected()) {
            try {
                emit(Status.Loading)

                val response = webServices.login(loginModel)
                emit(Status.Success(response))


            } catch (e: Throwable) {
                when (e) {
                    is HttpException -> {
                        val type = object : TypeToken<AuthResponse>() {}.type
                        val errorResponse: AuthResponse? =
                            gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                        Log.e("loginUsereeeee: ", errorResponse?.message.toString())
                        emit(Status.Error(errorResponse?.message.toString()))
                    }

                    is Exception -> {
                        Log.e("loginUsereeeee: ", e.message.toString())
                        emit(Status.Error(e.message.toString()))
                    }
                }
            }
        } else {
            emit(Status.Error("برجاء التحقق من الاتصال بالانترنت"))
        }

    }

    fun deleteProduct(id:String) = flow {
        if (NetworkUtils(context).isNetworkConnected()) {
            try {
                emit(Status.Loading)

                val response = webServices.deleteProduct(id)
                emit(Status.Success(response))


            } catch (e: Throwable) {
                when (e) {
                    is HttpException -> {
                        val type = object : TypeToken<AuthResponse>() {}.type
                        val errorResponse: AuthResponse? =
                            gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                        Log.e("loginUsereeeee: ", errorResponse?.message.toString())
                        emit(Status.Error(errorResponse?.message.toString()))
                    }

                    is Exception -> {
                        Log.e("loginUsereeeee: ", e.message.toString())
                        emit(Status.Error(e.message.toString()))
                    }
                }
            }
        } else {
            emit(Status.Error("برجاء التحقق من الاتصال بالانترنت"))
        }

    }

    fun getStatistics() = flow {
        if (NetworkUtils(context).isNetworkConnected()) {
            try {
                emit(Status.Loading)

                val response = webServices.getStatistics()
                emit(Status.Success(response))


            } catch (e: Throwable) {
                when (e) {
                    is HttpException -> {
                        val type = object : TypeToken<AuthResponse>() {}.type
                        val errorResponse: AuthResponse? =
                            gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                        Log.e("loginUsereeeee: ", errorResponse?.message.toString())
                        emit(Status.Error(errorResponse?.message.toString()))
                    }

                    is Exception -> {
                        Log.e("loginUsereeeee: ", e.message.toString())
                        emit(Status.Error(e.message.toString()))
                    }
                }
            }
        } else {
            emit(Status.Error("برجاء التحقق من الاتصال بالانترنت"))
        }

    }
}
package com.devYoussef.nb3elkhieradmin.data.remote

import android.content.Context
import android.net.Uri
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
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

    fun deleteProduct(id: String) = flow {
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

    fun getCategory() = flow {
        if (NetworkUtils(context).isNetworkConnected()) {
            try {
                emit(Status.Loading)

                val response = webServices.getCategory()
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

    fun getOneProduct(id: String) = flow {
        if (NetworkUtils(context).isNetworkConnected()) {
            try {
                emit(Status.Loading)

                val response = webServices.getOneProduct(id)
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

    fun addProduct(
        ctx: Context,
        fileUri: Uri,
        fileRealPath: String,
        name: String,
        price: String,
        shortDescription: String,
        country: String,
        category: String,
        originalPrice: String,
        quantity: String,
        isAvailable: Boolean,
        isOffer: Boolean,
        offerPrice: String,
        offerItemNum: String,
        priceCurrency: String
    ) = flow {
        emit(Status.Loading)
        if (NetworkUtils(ctx).isNetworkConnected()) {
            try {
                val fileToSend = prepareFilePart("image", fileRealPath, fileUri, ctx)
                val nameRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), name)
                val priceRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), price)
                val shortDescriptionRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), shortDescription)
                val countryRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), country)
                val categoryRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), category)
                val originalPriceRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), originalPrice)
                val quantityRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), quantity)
                val isAvailableRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), isAvailable.toString())
                val isOfferRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), isOffer.toString())
                val offerPriceRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), offerPrice)
                val offerItemNumRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), offerItemNum)
                val priceCurrencyRequestBody: RequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), priceCurrency)

                val response = webServices.addProduct(
                    image = fileToSend,
                    name = nameRequestBody,
                    price = priceRequestBody,
                    shortDescription = shortDescriptionRequestBody,
                    country = countryRequestBody,
                    category = categoryRequestBody,
                    originalPrice = originalPriceRequestBody,
                    quantity = quantityRequestBody,
                    isAvailable = isAvailableRequestBody,
                    isOffered = isOfferRequestBody,
                    priceOffered = offerPriceRequestBody,
                    itemNum = offerItemNumRequestBody,
                    priceCurrency = priceCurrencyRequestBody
                )
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
        } else{
            emit(Status.Error("برجاء التحقق من الاتصال بالانترنت"))
        }
    }

    private fun prepareFilePart(
        partName: String,
        fileRealPath: String,
        fileUri: Uri,
        ctx: Context
    ): MultipartBody.Part {
        val file: File = File(fileRealPath)
        val requestFile: RequestBody = RequestBody.create(
            ctx.contentResolver.getType(fileUri)!!.toMediaTypeOrNull(), file
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}
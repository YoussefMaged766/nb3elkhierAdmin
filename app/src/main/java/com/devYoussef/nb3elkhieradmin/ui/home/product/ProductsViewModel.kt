package com.devYoussef.nb3elkhieradmin.ui.home.product

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.devYoussef.nb3elkhieradmin.data.remote.ProductsPagingSource
import com.devYoussef.nb3elkhieradmin.data.remote.Repo
import com.devYoussef.nb3elkhieradmin.data.remote.SearchPagingSource
import com.devYoussef.nb3elkhieradmin.model.AuthState
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.utils.Status
import com.devYoussef.nb3elkhieradmin.utils.WebServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val webServices: WebServices,
    private val repo: Repo
) : ViewModel() {

    private val _fileName = MutableLiveData("")
    val fileName: LiveData<String>
        get() = _fileName

    // new added
    fun setFileName(name: String) {
        _fileName.value = name
    }

    private val _data: MutableStateFlow<PagingData<ProductResponse.Data>> =
        MutableStateFlow(PagingData.empty())
    val data = _data.asStateFlow()

    private val _dataProduct: MutableStateFlow<PagingData<ProductResponse.Data>> =
        MutableStateFlow(PagingData.empty())
    val dataProduct = _dataProduct.asStateFlow()

    private val _stateProduct = MutableStateFlow(AuthState())
    val stateProduct = _stateProduct.asStateFlow()

    private val _stateAddProduct = MutableStateFlow(AuthState())
    val stateAddProduct = _stateAddProduct.asStateFlow()

    private val _stateUpdateProduct = MutableStateFlow(AuthState())
    val stateUpdateProduct = _stateUpdateProduct.asStateFlow()

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _stateCategory = MutableStateFlow(AuthState())
    val stateCategory = _stateCategory.asStateFlow()

    init {
        getCategory()
    }

    fun deleteProduct(id:String) = viewModelScope.launch {
        repo.deleteProduct(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _state.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _state.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _state.update { result ->
                        result.copy(
                            isLoading = false,
                            error = status.message,
                            success = null,
                            status = null
                        )
                    }
                }
            }
        }
    }

    private fun getCategory() = viewModelScope.launch {
        repo.getCategory().collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateCategory.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateCategory.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            category = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateCategory.update { result ->
                        result.copy(
                            isLoading = false,
                            error = status.message,
                            status = null
                        )
                    }
                }
            }
        }
    }

     fun getOneProduct(id:String) = viewModelScope.launch {
        repo.getOneProduct(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateProduct.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateProduct.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            product = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateProduct.update { result ->
                        result.copy(
                            isLoading = false,
                            error = status.message,
                            status = null
                        )
                    }
                }
            }
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
        isOffered: Boolean,
        offerPrice: String,
        offerItemNum: String,
        priceCurrency: String
    )=viewModelScope.launch {
        repo.addProduct(
            ctx = ctx,
            fileUri = fileUri,
            fileRealPath = fileRealPath,
            name = name,
            price = price,
            shortDescription = shortDescription,
            country = country,
            category = category,
            originalPrice = originalPrice,
            quantity = quantity,
            isAvailable = isAvailable,
            isOffer = isOffered,
            offerPrice = offerPrice,
            offerItemNum = offerItemNum,
            priceCurrency = priceCurrency
        ).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateAddProduct.update { result ->
                        result.copy(isLoading = true,
                            error = null,
                            success = null,
                            status = null
                        )
                    }
                }

                is Status.Success -> {
                    _stateAddProduct.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = "تم اضافة المنتج بنجاح"
                        )
                    }
                }

                is Status.Error -> {
                    _stateAddProduct.update { result ->
                        result.copy(
                            isLoading = false,
                            error = status.message,
                            success = null,
                            status = null
                        )
                    }
                }
            }
        }
    }

    fun updateProduct(
        ctx: Context,
        fileUri: Uri?=null,
        fileRealPath: String?=null,
        name: String,
        price: String,
        shortDescription: String,
        country: String,
        category: String,
        originalPrice: String,
        quantity: String,
        isAvailable: Boolean,
        isOffered: Boolean,
        offerPrice: String,
        offerItemNum: String,
        priceCurrency: String,
        id:String
    )=viewModelScope.launch {
        repo.updateProduct(
            ctx = ctx,
            fileUri = fileUri,
            fileRealPath = fileRealPath,
            name = name,
            price = price,
            shortDescription = shortDescription,
            country = country,
            category = category,
            originalPrice = originalPrice,
            quantity = quantity,
            isAvailable = isAvailable,
            isOffer = isOffered,
            offerPrice = offerPrice,
            offerItemNum = offerItemNum,
            priceCurrency = priceCurrency,
            id = id
        ).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateUpdateProduct.update { result ->
                        result.copy(isLoading = true,
                            error = null,
                            success = null,
                            status = null
                        )
                    }
                }

                is Status.Success -> {
                    _stateUpdateProduct.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = "تم تعديل المنتج بنجاح"
                        )
                    }
                }

                is Status.Error -> {
                    _stateUpdateProduct.update { result ->
                        result.copy(
                            isLoading = false,
                            error = status.message,
                            success = null,
                            status = null
                        )
                    }
                }
            }
        }
    }


    fun getPagingSearchedProducts(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(
                    pageSize = 15,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    SearchPagingSource(
                        webServices = webServices,
                        query = query
                    )
                }

            ).flow.cachedIn(viewModelScope).collect {
                _data.value = it
            }
        }
    }

    fun getPagingProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(
                    pageSize = 15,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    ProductsPagingSource(
                        webServices = webServices,
                    )
                }

            ).flow.cachedIn(viewModelScope).collectLatest {
                _dataProduct.value = it

            }
        }
    }




}
package com.devYoussef.nb3elkhieradmin.ui.home.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val webServices: WebServices,
    private val repo: Repo
) : ViewModel() {

    private val _data: MutableStateFlow<PagingData<ProductResponse.Data>> =
        MutableStateFlow(PagingData.empty())
    val data = _data.asStateFlow()

    private val _dataProduct: MutableStateFlow<PagingData<ProductResponse.Data>> =
        MutableStateFlow(PagingData.empty())
    val dataProduct = _dataProduct.asStateFlow()

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

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

            ).flow.collect {
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

            ).flow.collect {
                _dataProduct.value = it
            }
        }
    }


}
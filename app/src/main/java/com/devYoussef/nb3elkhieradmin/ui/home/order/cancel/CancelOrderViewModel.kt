package com.devYoussef.nb3elkhieradmin.ui.home.order.cancel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devYoussef.nb3elkhieradmin.data.remote.Repo
import com.devYoussef.nb3elkhieradmin.model.AuthState
import com.devYoussef.nb3elkhieradmin.model.OrderResponse
import com.devYoussef.nb3elkhieradmin.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancelOrderViewModel @Inject constructor(private val repo: Repo) :ViewModel(){


    private val _stateOrder = MutableStateFlow(AuthState())
    val stateOrder = _stateOrder.asStateFlow()

    private val _filteredList = MutableStateFlow<List<OrderResponse.AllOrder>>(emptyList())
    val filteredList = _filteredList.asStateFlow()

    fun updateFilteredList(sortedList: List<OrderResponse.AllOrder>) {
        _filteredList.value = sortedList
    }

    fun getAllOrder() = viewModelScope.launch {
        repo.getAllOrders().collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateOrder.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateOrder.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            order = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateOrder.update { result ->
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
}
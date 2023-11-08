package com.devYoussef.nb3elkhieradmin.ui.home.order.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devYoussef.nb3elkhieradmin.data.remote.Repo
import com.devYoussef.nb3elkhieradmin.model.AuthState
import com.devYoussef.nb3elkhieradmin.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val repo: Repo):ViewModel() {

    private val _stateOrder = MutableStateFlow(AuthState())
    val stateOrder = _stateOrder.asStateFlow()

    private val _stateAcceptOrder = MutableStateFlow(AuthState())
    val stateAcceptOrder = _stateAcceptOrder.asStateFlow()

    private val _stateDeclineOrder = MutableStateFlow(AuthState())
    val stateDeclineOrder = _stateDeclineOrder.asStateFlow()

    private val _stateBlock = MutableStateFlow(AuthState())
    val stateBlock = _stateBlock.asStateFlow()

    fun getOrderDetails(id:String) = viewModelScope.launch {
        repo.getOrderDetails(id).collect { status ->
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
                            orderDetails = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateOrder.update { result ->
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

    fun acceptOrder(id:String) = viewModelScope.launch {
        repo.acceptOrder(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateAcceptOrder.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateAcceptOrder.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateAcceptOrder.update { result ->
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

    fun declineOrder(id:String) = viewModelScope.launch {
        repo.declineOrder(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateDeclineOrder.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateDeclineOrder.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateDeclineOrder.update { result ->
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

    fun blockUser(id:String) = viewModelScope.launch {
        repo.blockAndNonUser(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateBlock.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateBlock.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateBlock.update { result ->
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

}
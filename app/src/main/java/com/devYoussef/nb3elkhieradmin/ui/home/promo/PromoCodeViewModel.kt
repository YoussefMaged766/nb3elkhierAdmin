package com.devYoussef.nb3elkhieradmin.ui.home.promo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devYoussef.nb3elkhieradmin.data.remote.Repo
import com.devYoussef.nb3elkhieradmin.model.AuthState
import com.devYoussef.nb3elkhieradmin.model.PromoCodeModel
import com.devYoussef.nb3elkhieradmin.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromoCodeViewModel @Inject constructor(private val repo: Repo) : ViewModel() {

    private val _stateAddPromo = MutableStateFlow(AuthState())
    val stateAddPromo = _stateAddPromo.asStateFlow()

    private val _stateDeletePromo = MutableStateFlow(AuthState())
    val stateDeletePromo = _stateDeletePromo.asStateFlow()

    private val _stateUpdatePromo = MutableStateFlow(AuthState())
    val stateUpdatePromo = _stateUpdatePromo.asStateFlow()

    private val _stateAllPromo = MutableStateFlow(AuthState())
    val stateAllPromo = _stateAllPromo.asStateFlow()

    private val _stateGetOnePromo = MutableStateFlow(AuthState())
    val stateGetOnePromo = _stateGetOnePromo.asStateFlow()


    fun deletePromo(id:String) = viewModelScope.launch {
        repo.deletePromoCode(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateDeletePromo.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateDeletePromo.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateDeletePromo.update { result ->
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

    fun getOnePromo(id:String) = viewModelScope.launch {
        repo.getOnePromoCode(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateGetOnePromo.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateGetOnePromo.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            promo = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateGetOnePromo.update { result ->
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

    fun getAllPromo() = viewModelScope.launch {
        repo.getAllPromoCode().collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateAllPromo.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateAllPromo.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            promo = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateAllPromo.update { result ->
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

    fun addPromo(model:PromoCodeModel) = viewModelScope.launch {
        repo.addPromoCode(model).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateAddPromo.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateAddPromo.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateAddPromo.update { result ->
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

    fun updatePromo(model:PromoCodeModel , id:String) = viewModelScope.launch {
        repo.updatePromoCode(model , id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateUpdatePromo.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateUpdatePromo.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateUpdatePromo.update { result ->
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
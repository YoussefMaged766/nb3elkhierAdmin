package com.devYoussef.nb3elkhieradmin.ui.home.regions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devYoussef.nb3elkhieradmin.data.remote.Repo
import com.devYoussef.nb3elkhieradmin.model.AuthState
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import com.devYoussef.nb3elkhieradmin.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(private val repo: Repo) : ViewModel() {

    private val _stateAddRegions = MutableStateFlow(AuthState())
    val stateAddRegions = _stateAddRegions.asStateFlow()

    private val _stateDeleteRegions = MutableStateFlow(AuthState())
    val stateDeleteRegions = _stateDeleteRegions.asStateFlow()

    private val _stateUpdateRegions = MutableStateFlow(AuthState())
    val stateUpdateRegions = _stateUpdateRegions.asStateFlow()

    private val _stateAllRegions = MutableStateFlow(AuthState())
    val stateAllRegions = _stateAllRegions.asStateFlow()

    private val _stateGetOneRegions = MutableStateFlow(AuthState())
    val stateGetOneRegions = _stateGetOneRegions.asStateFlow()

    fun deleteRegion(id: String) = viewModelScope.launch {
        repo.deleteRegion(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateDeleteRegions.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateDeleteRegions.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateDeleteRegions.update { result ->
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

    fun getOneRegion(id: String) = viewModelScope.launch {
        repo.getOneRegion(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateGetOneRegions.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateGetOneRegions.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            region = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateGetOneRegions.update { result ->
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

    fun getAllRegions() = viewModelScope.launch {
        repo.getAllRegion().collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateAllRegions.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateAllRegions.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            region = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateAllRegions.update { result ->
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

    fun addRegion(model: LoginModel) = viewModelScope.launch {
        repo.addRegion(model).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateAddRegions.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateAddRegions.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateAddRegions.update { result ->
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

    fun updateRegion(model: LoginModel, id: String) = viewModelScope.launch {
        repo.updateRegion(model, id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateUpdateRegions.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateUpdateRegions.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateUpdateRegions.update { result ->
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
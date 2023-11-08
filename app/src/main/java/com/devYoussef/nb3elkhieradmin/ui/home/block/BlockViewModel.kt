package com.devYoussef.nb3elkhieradmin.ui.home.block

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
class BlockViewModel @Inject constructor(private val repo: Repo):ViewModel() {
    private val _stateGetAllBlock = MutableStateFlow(AuthState())
    val stateGetAllBlock = _stateGetAllBlock.asStateFlow()

    private val _stateUnBlock = MutableStateFlow(AuthState())
    val stateUnBlock = _stateUnBlock.asStateFlow()

    fun getAllBlocks() = viewModelScope.launch {
        repo.getAllBlockUsers().collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateGetAllBlock.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateGetAllBlock.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            block = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateGetAllBlock.update { result ->
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

    fun unBlockUser(id:String) = viewModelScope.launch {
        repo.blockAndNonUser(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateUnBlock.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateUnBlock.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = status.data.message.toString()
                        )
                    }
                }

                is Status.Error -> {
                    _stateUnBlock.update { result ->
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
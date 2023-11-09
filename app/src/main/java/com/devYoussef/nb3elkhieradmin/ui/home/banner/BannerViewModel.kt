package com.devYoussef.nb3elkhieradmin.ui.home.banner

import android.content.Context
import android.net.Uri
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
class BannerViewModel @Inject constructor(private val repo: Repo) : ViewModel() {

    private val _stateGetAllBanners = MutableStateFlow(AuthState())
    val stateGetAllBanners = _stateGetAllBanners.asStateFlow()

    private val _stateAddBanners = MutableStateFlow(AuthState())
    val stateAddBanners = _stateAddBanners.asStateFlow()

    private val _stateDeleteBanners = MutableStateFlow(AuthState())
    val stateDeleteBanners = _stateDeleteBanners.asStateFlow()

    fun getAllBanners() = viewModelScope.launch {
        repo.getAllBanners().collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateGetAllBanners.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateGetAllBanners.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            banner = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateGetAllBanners.update { result ->
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

    fun addBanners(
        ctx: Context,
        fileUri: Uri,
        fileRealPath: String
    ) = viewModelScope.launch {
        repo.addBanners(
            ctx = ctx,
            fileUri = fileUri,
            fileRealPath = fileRealPath
        ).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateAddBanners.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateAddBanners.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = "تم اضافة البانر بنجاح"
                        )
                    }
                }

                is Status.Error -> {
                    _stateAddBanners.update { result ->
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

    fun deleteBanners(id:String) = viewModelScope.launch {
        repo.deleteBanners(id).collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateDeleteBanners.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateDeleteBanners.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            success = "تم حذف البانر بنجاح"
                        )
                    }
                }

                is Status.Error -> {
                    _stateDeleteBanners.update { result ->
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
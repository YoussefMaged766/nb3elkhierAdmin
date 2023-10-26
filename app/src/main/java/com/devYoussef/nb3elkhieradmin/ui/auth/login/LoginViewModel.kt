package com.devYoussef.nb3elkhieradmin.ui.auth.login

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
class LoginViewModel @Inject constructor(private val repo: Repo):ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun loginIn(loginModel: LoginModel) = viewModelScope.launch {
        repo.loginIn(loginModel).collect { status ->
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
                            success = status.data.status.toString(),
                            error = null,
                            login = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _state.update { result ->
                        result.copy(isLoading = false,
                            error = status.message,
                            success = null
                        )
                    }
                }
            }
        }
    }
}
package com.devYoussef.nb3elkhieradmin.ui.home.users

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
class UsersViewModel @Inject constructor(private val repo: Repo):ViewModel() {
    private val _stateGetAllUsers = MutableStateFlow(AuthState())
    val stateGetAllUsers = _stateGetAllUsers.asStateFlow()



    fun getAllUsers() = viewModelScope.launch {
        repo.getAllUsers().collect { status ->
            when (status) {
                is Status.Loading -> {
                    _stateGetAllUsers.update { result ->
                        result.copy(isLoading = true)
                    }
                }

                is Status.Success -> {
                    _stateGetAllUsers.update { result ->
                        result.copy(
                            isLoading = false,
                            status = status.data.status.toString(),
                            error = null,
                            users = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _stateGetAllUsers.update { result ->
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
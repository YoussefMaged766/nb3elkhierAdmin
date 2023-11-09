package com.devYoussef.nb3elkhieradmin.ui.home.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devYoussef.nb3elkhieradmin.data.remote.Repo
import com.devYoussef.nb3elkhieradmin.model.AuthState
import com.devYoussef.nb3elkhieradmin.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val repo: Repo) : ViewModel() {

    private val linkFlow =
        MutableStateFlow("<iframe id=\"igraph\" scrolling=\"no\" style=\"border:none;\" seamless=\"seamless\" src=\"https://plotly.com/~Muhammed_Zidan/400.embed\" height=\"525\" width=\"100%\"></iframe>\n")

    fun setLink(link: String) {
        linkFlow.value = link
    }

    fun getLink(): StateFlow<String> = linkFlow


    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun getStatistics() = viewModelScope.launch {
        repo.getStatistics().collect { status ->
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
                            error = null,
                            success = status.data.message.toString(),
                            statistics = status.data
                        )
                    }
                }

                is Status.Error -> {
                    _state.update { result ->
                        result.copy(
                            isLoading = false,
                            error = status.message,
                            success = null,
                        )
                    }
                }
            }
        }
    }
}
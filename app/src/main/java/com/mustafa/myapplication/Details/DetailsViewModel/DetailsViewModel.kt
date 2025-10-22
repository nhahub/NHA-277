package com.mustafa.myapplication.Details.DetailsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.myapplication.Details.DetailsNetwork.DetailsRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class DetailsViewModel(
    private val DataSourse: DetailsRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state: StateFlow<DetailsUiState> = _state

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _state.value = DetailsUiState(isLoading = true)
            try {
                val movie = DataSourse.getMovieDetails(movieId)
                _state.value = DetailsUiState(movie = movie)
            } catch (e: Exception) {
                _state.value = DetailsUiState(error = e.message)
            }
        }
    }
}
package com.mustafa.myapplication.Details.DetailsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.Details.DetailsRepo.DetailsRepo
import com.mustafa.myapplication.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repo: DetailsRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState<Movie>>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState<Movie>> = _uiState

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(DetailUiState.Loading)

            val response = repo.getMovieDetails(movieId)
            when (response) {
                is DetailUiState.Success<*> -> {
                    try {
                        _uiState.emit(response as DetailUiState.Success<Movie>)
                    } catch (e: Exception) {
                        _uiState.emit(DetailUiState.Error("Something went wrong"))
                    }
                }
                is DetailUiState.Error -> _uiState.emit(response)
                else -> {}
            }
        }
    }
}

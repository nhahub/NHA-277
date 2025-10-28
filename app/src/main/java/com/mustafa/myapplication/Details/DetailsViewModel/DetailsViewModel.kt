package com.mustafa.myapplication.Details.DetailsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.Details.DetailsNetwork.DetailsRemoteDataSource
import com.mustafa.myapplication.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class DetailsViewModel(
    private val dataSourse: DetailsRemoteDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState<Movie>>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState<Movie>> = _uiState
    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(DetailUiState.Loading)

            val response = dataSourse.getMovieDetails(movieId)
            when (response) {
                is DetailUiState.Success<*> -> try {
                    _uiState.emit(response)
                } catch (e: Exception) {
                    _uiState.emit(DetailUiState.Error("Something went wrong"))

                }

                is DetailUiState.Error -> _uiState.emit(response)
                else -> {}
            }
        }
        fun addToFavorites(movie: Movie) {
            //  Database أو SharedPreferences
            println("Added ${movie.title} to favorites ❤️")
        }

        fun removeFromFavorites(movie: Movie) {
            println("Removed ${movie.title} from favorites 💔")
        }
    }
}
package com.mustafa.myapplication.Details.DetailsRepo

import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.model.Movie

interface DetailsRepo {
    suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie>
    suspend fun addToFavorites(movie: Movie)
    suspend fun removeFromFavorites(movieId: Int)
    suspend fun isFavorite(movieId: Int): Boolean
}
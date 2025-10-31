package com.mustafa.myapplication.Details.DetailsRepo

import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.Details.DetailsNetwork.DetailsRemoteDataSource
import com.mustafa.myapplication.model.Movie

class DetailsRepoImpl(
    private val remoteDataSource: DetailsRemoteDataSource
) : DetailsRepo {

    // هنا بنحتفظ بالمفضلات مؤقتًا في الذاكرة
    private val favorites = mutableListOf<Movie>()

    override suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie> {
        return try {
            remoteDataSource.getMovieDetails(movieId)
        } catch (e: Exception) {
            DetailUiState.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun addToFavorites(movie: Movie) {
        if (favorites.none { it.id == movie.id }) {
            favorites.add(movie)
        }
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        favorites.removeAll { it.id == movieId }
    }

    fun isFavorite(movieId: Int): Boolean = favorites.any { it.id == movieId }
}

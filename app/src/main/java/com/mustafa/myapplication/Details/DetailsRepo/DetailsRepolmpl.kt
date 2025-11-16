package com.mustafa.myapplication.Details.DetailsRepo

import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.Details.DetailsNetwork.DetailsRemoteDataSource
import com.mustafa.myapplication.model.Movie

class DetailsRepoImpl(
    private val remoteDataSource: DetailsRemoteDataSource
) : DetailsRepo {

    private val favorites = mutableListOf<Movie>()

    override suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie> {
        return remoteDataSource.getMovieDetails(movieId)
    }

    override suspend fun addToFavorites(movie: Movie) {
        if (favorites.none { it.id == movie.id }) {
            favorites.add(movie)
        }
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        favorites.removeAll { it.id == movieId }
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return favorites.any { it.id == movieId }
    }
}
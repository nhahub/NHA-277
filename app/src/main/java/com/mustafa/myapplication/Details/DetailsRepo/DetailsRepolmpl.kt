package com.mustafa.myapplication.Details.DetailsRepo

import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.Details.DetailsNetwork.DetailsRemoteDataSource
import com.mustafa.myapplication.model.Movie

class DetailsRepoImpl(
    private val remoteDataSource: DetailsRemoteDataSource
) : DetailsRepo {

    override suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie> {
        return try {
            remoteDataSource.getMovieDetails(movieId)
        } catch (e: Exception) {
            DetailUiState.Error(e.message ?: "Unknown error occurred")
        }
    }
    override suspend fun addToFavorites(movie: Movie): DetailUiState<String> {
        return try {
            // دي مكانها هتتعامل مع local DB أو SharedPreferences مثلاً
            // دلوقتي هنعملها mock (وهمية)
            println("Added to favorites: ${movie.title}")
            DetailUiState.Success("Movie added to favorites!")
        } catch (e: Exception) {
            DetailUiState.Error("Failed to add to favorites")
        }
    }
}

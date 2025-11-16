package com.mustafa.myapplication.Details.DetailsNetwork

import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.network.ApiClient
import com.mustafa.myapplication.model.Movie

class DetailsRemoteDataSourcelmpl : DetailsRemoteDataSource {

    override suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie> {
        return try {
            val response = ApiClient.api.getMovieDetails(movieId)

            if (response.isSuccessful) {
                val movie = response.body()
                    ?: return DetailUiState.Error("Empty response body")
                DetailUiState.Success(movie)
            } else {
                val errorMessage = when (response.code()) {
                    in 300..399 -> "Redirection error: ${response.code()}"
                    in 400..499 -> "Client error: ${response.code()} - ${response.message()}"
                    in 500..599 -> "Server error: ${response.code()} - ${response.message()}"
                    else -> "HTTP error: ${response.code()} - ${response.message()}"
                }
                DetailUiState.Error(errorMessage)
            }
        } catch (e: Exception) {
            DetailUiState.Error("Network error: ${e.localizedMessage ?: "Unknown error"}")
        }
    }
}
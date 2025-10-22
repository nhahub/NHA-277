package com.mustafa.myapplication.Details.DetailsNetwork

import com.mustafa.myapplication.Details.DetailUiState
import com.mustafa.myapplication.model.Movie

interface DetailsRemoteDataSource {

    suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie>
}
package com.mustafa.myapplication.Details.DetailsRepo

import com.mustafa.myapplication.Details.DetailsUI.DetailUiState
import com.mustafa.myapplication.Details.DetailsNetwork.DetailsRemoteDataSource
import com.mustafa.myapplication.model.Movie

class DetailsRepoImpl(
    private val remoteDataSource: DetailsRemoteDataSource
) : DetailsRepo {

    override suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie> {
        return remoteDataSource.getMovieDetails(movieId)
    }


}
package com.mustafa.myapplication.Home.Repo

import com.mustafa.myapplication.Home.HomeNetwork.HomeRemoteDataSource
import com.mustafa.myapplication.model.PopularResponse

class HomeRepoImpl(private val remoteDataSource: HomeRemoteDataSource) : HomeRepo {
    override suspend fun getPopularMovies(page: Int): Result<PopularResponse> {
        return try {
            val response = remoteDataSource.getPopularMovies(page = page)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
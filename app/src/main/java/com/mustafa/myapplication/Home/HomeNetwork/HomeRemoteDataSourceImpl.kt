package com.mustafa.myapplication.Home.HomeNetwork
import com.mustafa.myapplication.model.PopularResponse
import com.mustafa.myapplication.network.ApiService

class HomeRemoteDataSourceImpl(private val api: ApiService) : HomeRemoteDataSource {
    override suspend fun getPopularMovies(page: Int): PopularResponse {
        return api.getPopularMovies(page = page)
    }
}
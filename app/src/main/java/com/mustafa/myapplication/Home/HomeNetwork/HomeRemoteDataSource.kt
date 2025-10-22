package com.mustafa.myapplication.Home.HomeNetwork

import com.mustafa.myapplication.model.PopularResponse

interface HomeRemoteDataSource {
    suspend fun getPopularMovies(page: Int): PopularResponse
}
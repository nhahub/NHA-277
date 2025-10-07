package com.mustafa.myapplication.network

import com.mustafa.myapplication.model.PopularResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): PopularResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query : String ,
        @Query("language") language: String = "en_US" ,
        @Query("page") page :Int = 1
    ): PopularResponse
}
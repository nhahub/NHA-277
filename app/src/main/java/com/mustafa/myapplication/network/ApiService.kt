package com.mustafa.myapplication.network

import com.mustafa.myapplication.model.Movie
import com.mustafa.myapplication.model.PopularResponse
import com.mustafa.myapplication.model.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "172eaf4d804a5fd635f8b6741a164b92",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): PopularResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query : String ,
        @Query("language") language: String = "en_US" ,
        @Query("page") page :Int = 1
    ): Response<SearchResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): Response<Movie>
}
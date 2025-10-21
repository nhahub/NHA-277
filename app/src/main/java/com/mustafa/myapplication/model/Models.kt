package com.mustafa.myapplication.model

import com.google.gson.annotations.SerializedName

data class PopularResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
)

data class Movie(
    val id: Int,
    val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    val popularity: Double?
)


data class SearchResponse(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
)
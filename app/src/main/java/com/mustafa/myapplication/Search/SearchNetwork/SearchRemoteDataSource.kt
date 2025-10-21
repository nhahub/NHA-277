package com.mustafa.myapplication.Search.SearchNetwork

import com.mustafa.myapplication.Search.UiSearchState
import com.mustafa.myapplication.model.Movie

interface SearchRemoteDataSource {
    suspend fun searchMovies(query : String): UiSearchState<List<Movie>>
}
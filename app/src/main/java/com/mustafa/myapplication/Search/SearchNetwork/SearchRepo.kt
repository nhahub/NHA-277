package com.mustafa.myapplication.Search.SearchNetwork

import com.mustafa.myapplication.model.Movie

interface SearchRepo {
    suspend fun searchMovies(query : String) : List<Movie>
}
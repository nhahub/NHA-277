package com.mustafa.myapplication.Search.SearchRepo

import com.mustafa.myapplication.Search.UiSearchState
import com.mustafa.myapplication.model.Movie

interface SearchRepo {
    suspend fun searchMovies(query : String) : List<Movie>
}
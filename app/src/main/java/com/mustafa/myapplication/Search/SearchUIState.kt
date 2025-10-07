package com.mustafa.myapplication.Search

import com.mustafa.myapplication.model.Movie

data class SearchState (
    val query: String = " " ,
    val searchResults: List<Movie> = emptyList() ,
    val isLoading : Boolean ,
    val error: String? = null ,
    val searchIntiated : Boolean = false
    )

package com.mustafa.myapplication.Search.SearchRepo

import com.mustafa.myapplication.Search.SearchUI.UiSearchState

interface SearchRepo {
    suspend fun searchMovies(query : String) : UiSearchState
}
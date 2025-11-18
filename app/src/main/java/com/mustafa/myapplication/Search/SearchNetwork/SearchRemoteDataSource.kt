package com.mustafa.myapplication.Search.SearchNetwork

import com.mustafa.myapplication.Search.SearchUI.UiSearchState

interface SearchRemoteDataSource {
    suspend fun searchMovies(query : String): UiSearchState
}
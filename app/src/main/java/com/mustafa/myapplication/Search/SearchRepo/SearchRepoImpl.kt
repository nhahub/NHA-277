package com.mustafa.myapplication.Search.SearchRepo

import com.mustafa.myapplication.Search.SearchNetwork.SearchRemoteDataSource
import com.mustafa.myapplication.Search.SearchUI.UiSearchState

class SearchRepoImpl (private val searchRemoteDataSource: SearchRemoteDataSource) : SearchRepo {
    override suspend fun searchMovies(query: String): UiSearchState {
        return searchRemoteDataSource.searchMovies(query = query)
    }
}
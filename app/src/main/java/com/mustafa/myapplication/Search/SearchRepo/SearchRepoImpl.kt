package com.mustafa.myapplication.Search.SearchRepo

import com.mustafa.myapplication.Search.SearchNetwork.SearchRemoteDataSource
import com.mustafa.myapplication.Search.UiSearchState
import com.mustafa.myapplication.model.Movie

class SearchRepoImpl (private val searchRemoteDataSource: SearchRemoteDataSource) : SearchRepo {
    override suspend fun searchMovies(query: String): UiSearchState {
        return searchRemoteDataSource.searchMovies(query = query)
    }
}
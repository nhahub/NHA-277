package com.mustafa.myapplication.Search.SearchRepo

import com.mustafa.myapplication.Search.SearchNetwork.SearchRemoteDataSource
import com.mustafa.myapplication.model.Movie

class SearchRepoImpl (private val searchRemoteDataSource: SearchRemoteDataSource) : SearchRepo {
    override suspend fun searchMovies(query: String): List<Movie> {
        return searchRemoteDataSource.searchMovies(query = query)
    }
}
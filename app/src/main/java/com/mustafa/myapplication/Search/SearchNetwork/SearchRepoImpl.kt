package com.mustafa.myapplication.Search.SearchNetwork

import com.mustafa.myapplication.model.Movie

class SearchRepoImpl (private val searchRemoteDataSource: SearchRemoteDataSource) : SearchRepo {
    override suspend fun searchMovies(query: String): List<Movie> {
        return searchRemoteDataSource.searchMovies(query = query)
    }
}
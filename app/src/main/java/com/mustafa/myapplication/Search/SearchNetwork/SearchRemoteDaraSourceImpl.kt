package com.mustafa.myapplication.Search.SearchNetwork

import com.mustafa.myapplication.model.Movie
import com.mustafa.myapplication.network.ApiClient

class SearchRemoteDaraSourceImpl : SearchRemoteDataSource {
    override suspend fun searchMovies(query : String): List<Movie> {
        return try {
            val response = ApiClient.api.searchMovies(query = query)
            response.results
        }
        catch (e : Exception){
            emptyList()
        }
    }

}
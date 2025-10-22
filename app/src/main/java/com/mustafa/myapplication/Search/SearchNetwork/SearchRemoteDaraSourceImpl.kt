package com.mustafa.myapplication.Search.SearchNetwork

import com.mustafa.myapplication.Search.UiSearchState
import com.mustafa.myapplication.model.Movie
import com.mustafa.myapplication.network.ApiClient

class SearchRemoteDaraSourceImpl : SearchRemoteDataSource {
    override suspend fun searchMovies(query : String): List<Movie>{
        return try {
            val response = ApiClient.api.searchMovies(query = query)

            if (response.isSuccessful){
                val moviesList = response.body()?.results ?: emptyList()

                UiSearchState.Success(moviesList)
            } else{
                when(response.code()){
                    in 300..399 -> UiSearchState.Error("Redirection -> Bad Request : ${response.code()} - ${response.message()}")
                    in 400..499 -> UiSearchState.Error("Client Error : ${response.code()} - ${response.message()}")
                    in 500..599 -> UiSearchState.Error("Server Error : ${response.code()} - ${response.message()}")
                    else -> UiSearchState.Error("Error : ${response.code()} - ${response.message()}")
                }
            }

        }catch (e : Exception){
            UiSearchState.Error(e.localizedMessage ?: "Unknown Error")
        } as List<Movie>
    }

}
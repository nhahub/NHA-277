package com.mustafa.myapplication.Details.DetailsNetwork

import com.mustafa.myapplication.Details.DetailUiState
import com.mustafa.myapplication.network.ApiClient
import com.mustafa.myapplication.model.Movie

class DetailsRemoteDataSourcelmpl : DetailsRemoteDataSource {

    override suspend fun getMovieDetails(movieId: Int): DetailUiState<Movie> {
        return try {
            val response = ApiClient.api.getMovieDetails(movieId)
            if (response.isSuccessful){
                val movie = response.body() ?: throw Exception("Empty Response Body")
                DetailUiState.Success(movie)
            }else{
                when(response.code()){
                in 300..399 -> DetailUiState.Error("Redirection -> Bad Request : ${response.code()} - ${response.message()}")
                in 400..499 -> DetailUiState.Error("Client Error : ${response.code()} - ${response.message()}")
                in 500..599 -> DetailUiState.Error("Server Error : ${response.code()} - ${response.message()}")
                else -> DetailUiState.Error("Error : ${response.code()} - ${response.message()}")
            }
            }
        }catch (e: java.lang.Exception){
            DetailUiState.Error("UnKnown Message")
        }

    }

}
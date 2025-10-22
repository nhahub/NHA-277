package com.mustafa.myapplication.Details

import com.mustafa.myapplication.model.Movie

sealed class DetailUiState<out T> {

    object Loading : DetailUiState<Nothing>() // While getting the list from the network
    data class Success<T>(val data : T) : DetailUiState<T>() // successfully get the list
    data class Error(val message : String) : DetailUiState<Nothing>() // error happened
    companion object {
        val Idle: DetailUiState<Movie>
    }
}
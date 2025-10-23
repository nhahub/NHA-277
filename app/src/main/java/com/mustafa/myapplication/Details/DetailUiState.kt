package com.mustafa.myapplication.Details


sealed class DetailUiState<out T> {

    object Loading : DetailUiState<Nothing>() // While getting the list from the network
    data class Success<T>(val data : T) : DetailUiState<T>() // successfully get the list
    data class Error(val message : String) : DetailUiState<Nothing>() // error happened

}
package com.mustafa.myapplication.Search

sealed class UiSearchState<out T> {

    object Idle : UiSearchState<Nothing>() // Have not yet start searching

    object Empty : UiSearchState<Nothing>() // no Movie list found

    object Loading : UiSearchState<Nothing>() // While getting the list from the network
    data class Success<T>(val data : T) : UiSearchState<T>() // successfully get the list
    data class Error(val message : String) : UiSearchState<Nothing>() // error happened
}
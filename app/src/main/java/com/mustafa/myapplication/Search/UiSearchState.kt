package com.mustafa.myapplication.Search

import com.mustafa.myapplication.model.Movie

sealed class UiSearchState<out T>{

    object Idle : UiSearchState<Nothing>() // Have not yet start searching

    object Empty : UiSearchState<Nothing>() // no Movie list found

    object Loading : UiSearchState<Nothing>() // While getting the list from the network
    data class Success<T>(val movies : List<T>) : UiSearchState<List<T>>() // successfully get the list
    data class Error(val message : String) : UiSearchState<Nothing>() // error happened
}

data class SearchQuery(val text : String = "")
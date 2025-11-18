package com.mustafa.myapplication.Search.SearchUI

import com.mustafa.myapplication.model.Movie

sealed class UiSearchState{

    object Idle : UiSearchState() // Have not yet start searching

    object Empty : UiSearchState() // no Movie list found

    object Loading : UiSearchState() // While getting the list from the network
    data class Success(val movies :  List<Movie>) : UiSearchState() // successfully get the list
    data class Error(val message : String) : UiSearchState() // error happened
}

data class SearchQuery(val text : String = "")
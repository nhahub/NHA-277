package com.mustafa.myapplication.Search.SearchViewModel

import SearchViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mustafa.myapplication.Search.SearchRepo.SearchRepo

class SearchViewModelFactory(private val searchRepo: SearchRepo) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
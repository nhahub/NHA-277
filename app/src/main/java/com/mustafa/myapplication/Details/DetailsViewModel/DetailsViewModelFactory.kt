package com.mustafa.myapplication.Details.DetailsViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mustafa.myapplication.Details.DetailsRepo.DetailsRepo

class DetailsViewModelFactory(
    private val repo: DetailsRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
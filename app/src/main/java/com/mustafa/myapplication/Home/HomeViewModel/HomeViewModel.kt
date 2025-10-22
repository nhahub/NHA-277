package com.mustafa.myapplication.Home.HomeViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafa.myapplication.Home.Repo.HomeRepo
import com.mustafa.myapplication.model.Movie
import com.mustafa.myapplication.model.PopularResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepo) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchPopular()
    }

    fun fetchPopular(page: Int = 1) {
        viewModelScope.launch {
            _loading.value = true
            Log.d(TAG, "fetchPopular: starting request for page=$page")
            val result = repository.getPopularMovies(page)
            if (result.isSuccess) {
                val data: PopularResponse? = result.getOrNull()
                val list = data?.results ?: emptyList()
                _movies.value = list
                _error.value = null
                Log.d(TAG, "fetchPopular: success, movies count = ${list.size}")
                data?.results?.firstOrNull()?.let { movie ->
                    Log.d(TAG, "first movie -> id=${movie.id}, title=${movie.title}, poster=${movie.posterPath}")
                }
            } else {
                val ex = result.exceptionOrNull()
                _error.value = ex?.localizedMessage ?: "Unknown error"
                Log.e(TAG, "fetchPopular: failed", ex)
            }
            _loading.value = false
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
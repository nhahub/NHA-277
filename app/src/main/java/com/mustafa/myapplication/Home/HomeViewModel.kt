package com.mustafa.myapplication.Home

import android.util.Log
import androidx.lifecycle.*
import com.mustafa.myapplication.model.Movie
import com.mustafa.myapplication.model.PopularResponse
import kotlinx.coroutines.launch
class HomeViewModel(private val repository: HomeRepo) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchPopular()
    }

    fun fetchPopular(page: Int = 1) {
        viewModelScope.launch {
            _loading.postValue(true)
            Log.d(TAG, "fetchPopular: starting request for page=$page")
            val result = repository.getPopularMovies(page)
            if (result.isSuccess) {
                val data: PopularResponse? = result.getOrNull()
                val list = data?.results ?: emptyList()
                _movies.postValue(list)
                _error.postValue(null)
                Log.d(TAG, "fetchPopular: success, movies count = ${list.size}")
                data?.results?.firstOrNull()?.let { movie ->
                    Log.d(TAG, "first movie -> id=${movie.id}, title=${movie.title}, poster=${movie.posterPath}")
                }
            } else {
                val ex = result.exceptionOrNull()
                _error.postValue(ex?.localizedMessage ?: "Unknown error")
                Log.e(TAG, "fetchPopular: failed", ex)
            }
            _loading.postValue(false)
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
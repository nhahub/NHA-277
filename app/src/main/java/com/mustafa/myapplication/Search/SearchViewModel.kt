package com.mustafa.myapplication.Search

import androidx.lifecycle.ViewModel
import com.mustafa.myapplication.Search.SearchNetwork.SearchRepo
import androidx.lifecycle.viewModelScope
import com.mustafa.myapplication.Search.SearchNetwork.SearchRepoImpl
import com.mustafa.myapplication.model.Movie
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.http.Query
import java.io.IOException
import android.util.Log

private const val TAG = "SearchViewModel"

class SearchViewModel(private val searchRepo: SearchRepo) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchState(query = "", searchResults = emptyList(), isLoading = false, error = null, searchIntiated = false))
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    init {
        Log.d(TAG, "SearchViewModel init")
        setUpSearchDebounce()
    }

    fun onQueryChanged(newQuery: String) {
        Log.d(TAG, "onQueryChanged: $newQuery")
        searchQuery.value=newQuery
    }

    @OptIn(FlowPreview::class)
    private fun setUpSearchDebounce(){
        searchQuery.debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                Log.d(TAG, "setUpSearchDebounce: $query")
             _uiState.value = _uiState.value.copy(query = query)
                performSearch(query)}
            .launchIn(viewModelScope)
    }

    private suspend fun performSearch(query: String) {
        Log.d(TAG, "performSearch: $query")
        val trimmedQuery = query.trim()

        if (trimmedQuery.isEmpty()) {
            Log.d(TAG, "performSearch: query is empty")
            _uiState.value = _uiState.value.copy(
                searchIntiated = false,
                searchResults = emptyList(),
                isLoading = false,
                error = null
            )
            return
        }
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            searchIntiated = true,
            error = null
        )
        try {
            Log.d(TAG, "performSearch: searching for $trimmedQuery")
            val response = searchRepo.searchMovies(query = trimmedQuery)
            Log.d(TAG, "performSearch: found ${response.size} movies")

            _uiState.value = _uiState.value.copy(
                searchResults = response,
                isLoading = false,
                error = if (response.isEmpty()) "No movies found" else null
            )
        }catch (e:IOException) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isLoading = false, error = "Network error. Check your connection."
            )
        }catch (e:Exception){
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isLoading = false, error = "Something went wrong: ${e.message}"
            )
        }

    }

    fun searchMovies() {
        viewModelScope.launch {
            performSearch(_uiState.value.query)
        }
    }

    fun clearSearch(){
        searchQuery.value=""
    }

}